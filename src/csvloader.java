
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class csvloader {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File file = new File("C:/Users/ghfiy/PycharmProjects/GetOldTweets-python/Fusarium3-8.2016.csv");
		int ID = 0;
//		String keyword;
//		String user = null;
//		String msg = null;
//		String location = null;
//		Date date = null;
//		
//		
//		String countries = null;
		String userdesc = null;
		Scanner scan = null;
		Tweetinfoprocessing tip = new Tweetinfoprocessing();
		int counter = 0;
		ArrayList loc=new ArrayList<>();
		try {
			scan = new Scanner(file);

			while (scan.hasNextLine()) {
				System.out.println("-----------------New Line-----------------------");
				String line = scan.nextLine();
				System.out.println(line);				
				String[] lineArray = line.split(";");
				
				if (lineArray.length >= 4) {
					if ((lineArray[2].startsWith("7")) || (lineArray[2].startsWith("8"))){

						counter++;
					
					String keyword = null;
					String user = null;
					String msg = null;
					String location = null;
					Date date = null;
					
					
					String countries = null;
					for (int i = 0; i < lineArray.length; i++) {
						ID = counter;
						//System.out.println(ID);
						msg = lineArray[0];
						//System.out.println(msg);

						//DateFormat format = new SimpleDateFormat("DD/MM/YYYY", Locale.ENGLISH);

						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						java.util.Date dtt = df.parse(lineArray[1]);
						date = new java.sql.Date(dtt.getTime());

						keyword = lineArray[3];
						//System.out.println(keyword);

						user = lineArray[4];
						//System.out.println(user);
						if (hasIndex(5, lineArray) == true) {
							location = lineArray[5];
							
							//System.out.println(location);
						}else{
							location="";
						}

						if (hasIndex(6, lineArray) == true) {
							lineArray[6].replace("]", "");
							lineArray[6].replace("[", "");
							Matcher matcher = Pattern.compile("[\"'](.+)[\"']").matcher(lineArray[6]);
							if (matcher.find()) {
								countries = (matcher.group(1));
								//System.out.println(countries);
							}

						}

						// System.out.println(ID+"\t"+msg+"\t"+date+"\t"+user+"\t"+location+"\t"+countries);

					}
					//System.out.println(ID+"\t"+keyword+"\t"+msg+"\t"+date+"\t"+user+"\t"+location);
//					if(location.isEmpty()){
//						loc.add("");
//					}else{
					loc.add(location);
					DataTransfer dt=new DataTransfer();
					Geodata gd=new Geodata();
					//String username="ksuhiyp";
					ArrayList g=gd.geoest(location);
					String place = null;
					String country = null;
					double lat = 0;
					double lang = 0;
					for(int i=0;i<g.size();i++){
						place=(String) g.get(0);
						country=(String) g.get(1);
						lat=(double) g.get(2);
						lang=(double) g.get(3);
					}
					System.out.println(ID+"\t"+keyword+"\t"+msg+"\t"+date+"\t"+user+"\t"+location+"\t"+country+"\t"+place+"\t"+lat+"\t"+lang);
					dt.insertdatafromtext(ID, keyword, user, msg, location, date, country, userdesc,place,lat,lang);
				}
			}
				
			System.out.println(counter);
		} 
			
			}catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scan.close();
		}
	}

	public static boolean hasIndex(int index, String[] lineArray) {
		if (index < lineArray.length)
			return true;
		return false;
	}
}
