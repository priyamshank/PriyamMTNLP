import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class gplaceupdation {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = MySingleTon.getInstance();
		Statement stmt = conn.createStatement();
		String query="select * from cstweets where userlocation not like '' and gplace is null";
		ResultSet rs = stmt.executeQuery(query);
		String place = null;
		String country = null;
		double lat = 0;
		double lang = 0;
		boolean agriloc = false;
		boolean validloc = false;
		DataTransfer dt=new DataTransfer();
		int id;
		 while(rs.next()){
			 Geodata gd=new Geodata();
				id=rs.getInt("ID");
				ArrayList<Geoinfo> g=new ArrayList();
				System.out.println("=====================New Location=========================");
				String userlocation;
				
				userlocation=rs.getString("userlocation");
				if(userlocation == "USA"){
					dt.insertgdata("United States", id, "United States", 0.0, 0.0,false,false);
				}
				if(userlocation == "UK"){
					dt.insertgdata("United Kingdom", id, "United Kingdom", 0.0, 0.0,false,false);
				}
				else{
				if(URPdetection(userlocation)==false){
				userlocation=userlocation.replaceAll("[^,a-zA-Z ]", "");
				System.out.println(userlocation);
				g=gd.geoest(userlocation);
				
				for(Geoinfo item:g){
					System.out.println("Printing geoinfo item");
					place=item.getPlace();
					country=item.getCountry();
					lat=item.getLat();
					lang=item.getLang();
					agriloc=item.isAgriloc();
					validloc=item.isValidloc();
					System.out.println(item.getPlace()+" "+
					item.getCountry()+" "+
					item.getLat()+" "+
					item.getLang()+" "+
					item.isAgriloc()+" "+
					item.isValidloc());
//					place=(String) g.get(0);
//					country=(String) g.get(1);
//					lat=(double) g.get(2);
//					lang=(double) g.get(3);
				}
				
				
		 
				
			//	System.out.println(place+ " "+country);
			//	if(place!=null && country!=null)
				dt.insertgdata(place, id, country, lat, lang,agriloc,validloc);
				}
				else{
				
				dt.insertgdata("URP", id, "NA", 0.0, 0.0,false,false);
				}
				g.clear();
				
	

}
		 }
	}
	public static boolean URPdetection(String loc){
		String[] stopwords = {" { "," ","a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost",
		        "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and",
		        "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became",
		        "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides",
		        "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt",
		        "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else",
		        "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few",
		        "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from",
		        "front", "full", "further", "get", "give", "go", "had", "has", "hasnt",
		        "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
		        "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into",
		        "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many",
		        "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
		        "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none",
		        "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto",
		        "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps",
		        "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she",
		        "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something",
		        "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their",
		        "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
		        "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru",
		        "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
		        "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever",
		        "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
		        "whether", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet",
		        "you", "your", "yours", "yourself", "yourselves","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1.", "2.", "3.", "4.", "5.", "6.", "11",
		           "7.", "8.", "9.", "12", "13", "14",
		        "terms", "CONDITIONS", "conditions", "values", "interested.", "care", "sure", ".", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", "]", ":", ";", "<", ".", ">", "/", "?", "_", "-", "+", "=",
		        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
		        "contact", "buyers", "tried", "said,", "plan", "value", "principle.", "forces", "sent", "is,", "was", "like",
		        "discussion", "tmus", "diffrent.", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft.", "http://", "km", "miles"};
		boolean goodterm = false;
		Scanner content = new Scanner(loc);
		String s1;
        while (content.hasNext()) {
        	s1 = content.next();
			s1 = s1.toLowerCase();
			outerloop:
		for (int i = 0; i < stopwords.length; i++)
        {
            if (s1.equalsIgnoreCase(stopwords[i])) 
            {
                goodterm=true;
                System.out.println(loc+ " is a illegal location name");
                break outerloop;
                
            }
          
        }
		
	}
		return goodterm;
}
}


