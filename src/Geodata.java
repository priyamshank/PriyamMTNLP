import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.geonames.FeatureClass;
import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.geonames.WikipediaArticle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import twitter4j.conf.ConfigurationBuilder;

public class Geodata {

	public ArrayList<Geoinfo> geoest(String locnames) throws Exception {
		ArrayList ginfo = new ArrayList();
		ArrayList greturn = new ArrayList();
		ArrayList ginter = new ArrayList();
		ArrayList<Geoinfo> gi = new ArrayList();
		boolean validloc = false;
		ginfo.clear();
		try {
			String host = "10.185.190.10";
			String port = "8080";
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", port);
			System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
			WebService.setUserName("twitbaymas");
			// priyamshank20
			// ksuhiyp
			//twitbaymas
			// twitbay
			
			String place;
			String country;
			String newlocname;
			ginfo = geofulltext(locnames);
			if (ginfo.size() > 0) {
				System.out.println("------------user location search returned some result-------------");
				if (locnames.toLowerCase().contains(ginfo.get(0).toString().toLowerCase())
						|| locnames.toLowerCase().contains(ginfo.get(1).toString().toLowerCase())) {
					System.out.println("Valid user location");
					validloc = true;
					ginter = ginfo;
					greturn = ginter;

				} else {
					System.out.println("------------invalid user location---------------");
					// newlocname=geowiki(locnames);
					ginter = geowiki(locnames);
					if (ginter.isEmpty()) {
						greturn.clear();
						greturn.add(0, "URP");
						greturn.add(1, "NA");
						greturn.add(2, 0.0);
						greturn.add(3, 0.0);
						greturn.add(4, false);
						greturn.add(5, false);
					} else {
						greturn = ginter;
					}
				}
			} else {
				System.out.println("-------------user location search returned no result------------- ");
				// newlocname=geowiki(locnames);
				ginter = geowiki(locnames);
				if (ginter.size() == 0) {
					greturn.clear();
					greturn.add(0, "URP");
					greturn.add(1, "NA");
					greturn.add(2, 0.0);
					greturn.add(3, 0.0);
					greturn.add(4, false);
					greturn.add(5, false);
					
				} else {
					greturn = ginter;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Geoinfo item = new Geoinfo((String) greturn.get(0), (String) greturn.get(1), (double) greturn.get(2),
				(double) greturn.get(3), (boolean) greturn.get(4), validloc);
		gi.add(item);
		return gi;

	}

	public ArrayList geofulltext(String locnames) throws Exception {
		System.out.println("!!!!!!!!!!!!!!! calling text search !!!!!!!!!!!!!!!!!!!!!!");
		ArrayList ginfo = new ArrayList();
		boolean agriloc = false;
		ginfo.clear();
		if (locnames != null) {
			ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
			System.out.println("User location is   " + locnames);
			searchCriteria.setQ(locnames);
			searchCriteria.setFuzzy(0.0);
			searchCriteria.setStyle(Style.LONG);
			ToponymSearchResult searchResult1 = WebService.search(searchCriteria);
			outerloop: if (searchResult1.getToponyms().isEmpty() == false) {
				for (int i1 = 0; i1 < 50; i1++) {
					Toponym toponym = searchResult1.getToponyms().get(i1);

					if (toponym.getFeatureClass().equals(FeatureClass.P)
							|| toponym.getFeatureClass().equals(FeatureClass.A)
							|| toponym.getFeatureClass().equals(FeatureClass.L)
							|| toponym.getFeatureClass().equals(FeatureClass.V)) {
						if (toponym.getFeatureCode() == "PPLF" || toponym.getFeatureClass().equals(FeatureClass.L)
								|| toponym.getFeatureClass().equals(FeatureClass.V)) {
							System.out.println("Yay! Its a agri land tweet");
							agriloc = true;
						}
						System.out.println(toponym);
						ginfo.add(toponym.getName());
						ginfo.add(toponym.getCountryName());
						ginfo.add(toponym.getLatitude());
						ginfo.add(toponym.getLongitude());
						ginfo.add(agriloc);
						break outerloop;
					} else {
						break outerloop;
					}
				}
			}
		}
		return ginfo;
	}
	
	public ArrayList geowiki(String uloc) throws Exception {
		System.out.println("\\\\\\\\\\\\Caling wiki\\\\\\\\\\\\\\\\");
		String articlename = null;
		ArrayList gwiki = new ArrayList();
		List<WikipediaArticle> searchResult11 = WebService.wikipediaSearch(uloc, "en");
		outerloop: if (searchResult11.isEmpty() == false) {
			for (int i1 = 0; i1 < searchResult11.size(); i1++) {
				String fea = searchResult11.get(i1).getFeature();
				if (fea.contentEquals("city") || fea.contentEquals("country") || fea.contentEquals("adm1st")
						|| fea.contentEquals("adm2nd") || fea.contentEquals("adm3rd")) {
					// System.out.println(searchResult11.get(i1).getFeature());
					System.out.println("article title: " + searchResult11.get(i1).getTitle());
					articlename = searchResult11.get(i1).getTitle();

					System.out.println(
							searchResult11.get(i1).getLatitude() + " " + searchResult11.get(i1).getLongitude());
					gwiki = ll2place(searchResult11.get(i1).getLatitude(), searchResult11.get(i1).getLongitude());
					break outerloop;
				}
			}
		}
		return gwiki;

	}

	public ArrayList ll2place(double lat, double lang) throws IOException, Exception {
		System.out.println("entered ll2place");
		ArrayList ginfo = new ArrayList();
		boolean agriloc = false;
		List<Toponym> searchResult1 = WebService.findNearbyPlaceName(lat, lang);
		if(searchResult1.size()>0){
		for (int i1 = 0; i1 < 1; i1++) {
			String country = searchResult1.get(i1).getCountryName();
			String place = searchResult1.get(i1).getName();
			System.out.println(place + "  " + country);
			if (searchResult1.get(i1).getFeatureCode() == "PPLF"
					|| searchResult1.get(i1).getFeatureClass().equals(FeatureClass.L)
					|| searchResult1.get(i1).getFeatureClass().equals(FeatureClass.V)) {
				agriloc = true;

			}
			ginfo.add(searchResult1.get(i1).getName());
			ginfo.add(searchResult1.get(i1).getCountryName());
			ginfo.add(searchResult1.get(i1).getLatitude());
			ginfo.add(searchResult1.get(i1).getLongitude());
			ginfo.add(agriloc);
		}
		
	}
		return ginfo;
}
}