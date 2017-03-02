import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

		public class gtest {

	
			public static void main(String[] args) {
		// TODO Auto-generated method stub

		
				try {
					 String host = "10.185.190.10";
					    String port = "8080";
					    System.out.println("Using proxy: " + host + ":" + port);
					    System.setProperty("http.proxyHost", host);
					    System.setProperty("http.proxyPort", port);
					    System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
					WebService.setUserName("ksuhiyp"); // add your username here
					
					  ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
					  searchCriteria.setQ("zurich");
					  ToponymSearchResult searchResult = WebService.search(searchCriteria);
					  for(int i=0;i<1;i++){
						  Toponym toponym = searchResult.getToponyms().get(i);
					  
					     System.out.println(toponym.getName()+" "+ toponym.getCountryName());
					  }

					
				
			//	return loc;

				}catch(Exception e){
					e.printStackTrace();
				}

		

		}

		}
	
	

