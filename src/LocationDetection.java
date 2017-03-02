import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;


public class LocationDetection {

	public LocationDT locationfinder(String subject) throws JSONException, IOException {
		
		
		System.out.println("==================the user location is "+ subject+"===================================");
	    String res="";     
	    String newsubject="";
	    String[] words;
	    List<String> country=new ArrayList<>();
		String host = "10.185.190.10";
	    String port = "8080";
	    System.out.println("Using proxy: " + host + ":" + port);
	    System.setProperty("HTTP_PROXY", "10.185.190.10:8080");
	    System.setProperty("https.proxyPort", port);
	    System.setProperty("https.proxyHost", host);
		System.setProperty("HTTPS_PROXY", "10.185.190.10:8080");
		
		 List country1=new ArrayList();
		 List country2=new ArrayList();
		 List country3=new ArrayList();
		 List<LocationDT> ti=new ArrayList<>();
		 List<String> result=new ArrayList<>();
		 List initialsearch=new ArrayList<>();
		 LocationDT dt = null;
		 initialsearch=fileop(subject.replace(",", ""));
		 initialsearch.removeAll(Arrays.asList(null,""));
		 result=listjoin(initialsearch,country);
		 dt=new LocationDT(result,subject);
		
		 if(initialsearch.isEmpty()){
		 JSONArray arr1=new JSONArray();
		 JSONArray arr11=new JSONArray();
			 JSONArray arr=callwiki(subject.replaceAll(",\\s+",","));
			// JSONArray arr=callwiki(subject.replaceAll(",", " "));
			 if(arr.length()>0){
			 newsubject=arr.get(0).toString();
			 arr1=callwiki(newsubject);
			
			 }
			
			 country1=fileop(arr1.get(1).toString());
			 country2=fileop(arr1.get(2).toString());
			 res=arr1.get(2).toString();
			 List newList=listjoin(country1,country2);
			 newList.removeAll(Arrays.asList(null,""));
			 if(!newList.isEmpty()){
			// countryprint(newList);
			 
			 }
			 String[] queryterms=null;
			 if(newList.isEmpty()){
				 if(subject.indexOf(',')!=-1){
						System.out.println("comma found");
						queryterms=subject.split(",");
						

					for(int i=0;i<queryterms.length;i++){
						List temp=new ArrayList();
						System.out.println("individual search query is "+queryterms[i]);
						arr11=callwiki(queryterms[i]);
						if(arr11.length()>0){
							
						temp= fileop(arr11.get(2).toString());
						res=arr11.get(2).toString();
						country3.addAll(temp);
						
						}
					}
				}
			 }

			 List<String> newList1=listjoin(country3, country);
			 newList1.removeAll(Arrays.asList(null,""));
			 if(!newList1.isEmpty()){
			//		countryprint(newList1);
			 }
			result=listjoin(newList,newList1);
			// countryprint(result);
		 
			 dt=new LocationDT(result,res.replaceAll("\"", ""));
			 ti.add(dt);
		 }
			// return result;
			return dt;

			 }
		
		
	
	
		 
		

		 
	public List fileop(String text) throws FileNotFoundException{
		ArrayList countryfound=new ArrayList();
		System.out.println("text recieved by fileop"+text);
		if(text.contains("From a US postal abbreviation: This is a redirect from a US postal abbreviation to its associated municipality.")){
			countryfound.add("United States");
			
		}
		if(org.apache.commons.lang3.StringUtils.containsIgnoreCase(text, "England")||text.contains("UK")){
			countryfound.add("United Kingdom");
		}
		if(text.contains("USA")||text.contains("U.S.")){
			countryfound.add("United States");
		}
		else{
		try (Scanner sc = new Scanner(new BufferedReader(
                new FileReader("Countries")))) {
		 while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] germansyns = line.split(";");
                for(int i=0;i<=4;i++){
               
                    if (text.contains(germansyns[i])) {
                      
                        	countryfound.add(germansyns[i].replaceAll("^\"|\"$", ""));
                        }
                        
                    }
                }

            }
		}
		return countryfound;
		 }

	
	public JSONArray callwiki(String subject) throws IOException, JSONException{
			URL url = new URL("https://en.wikipedia.org/w/api.php?action=opensearch&limit=3&format=json&search="+ subject.replace(" ", "%20"));
			String text = "";
			System.out.println("subject recived by callwiki  "+subject);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
			    String line = null;
			    while (null != (line = br.readLine())) {
			        line = line.trim();
			        if (true) {
			            text += line;
			        }
			    }
			}

			JSONArray arr=new JSONArray(text);
			return arr;
	}
	
	public void countryprint(List country){
	
		country.removeAll(Arrays.asList(null,""));
		
		System.out.println("contries found are :");
		
		for(int i=0;i<country.size();i++){
			System.out.println(country.get(i));
		}
	}
	
	public List listjoin(List a,List b){
	 Set setboth = new HashSet(a);
	 setboth.addAll(b);
	 a.clear();
	 a.addAll(setboth);
	 List<String> newList = new ArrayList<String>(setboth);
	return newList;
	
	}
}
