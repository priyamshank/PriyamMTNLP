import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class msgchecker {

	public boolean mgscheck(String tweetmsg) throws IOException{
		boolean res = false;
		BufferedReader buf = new BufferedReader(new FileReader("CropData"));
        ArrayList<String> words = new ArrayList<>();
        String lineJustFetched = null;
        String[] wordsArray;

        while(true){
            lineJustFetched = buf.readLine();
            if(lineJustFetched == null){  
                break; 
            }else{
                wordsArray = lineJustFetched.split("[\\p{Punct}\\s]+");
                wordsArray = lineJustFetched.split("-");
                for(String each : wordsArray){
                    if(!"".equals(each)){
                        words.add(each);
                    }
                }
            }
        }
        Set<String> se =new HashSet<String>(words);
        words.clear();
        words = new ArrayList<String>(se);
        for (String ls : words){
        	if(tweetmsg.toLowerCase().contains(ls.toLowerCase())){
        		res = true;
        	}
        	else{
        		res= false;
        	}
  }
		return res;

      
	
		}
	public boolean usercheck(String user) throws IOException{
		boolean res = false;
		BufferedReader buf = new BufferedReader(new FileReader("Keywords"));
        ArrayList<String> words = new ArrayList<>();
        String lineJustFetched = null;
        String[] wordsArray;

        while(true){
            lineJustFetched = buf.readLine();
            if(lineJustFetched == null){  
                break; 
            }else{
                wordsArray = lineJustFetched.split("[\\p{Punct}\\s]+");
                wordsArray = lineJustFetched.split("-");
                for(String each : wordsArray){
                    if(!"".equals(each)){
                        words.add(each);
                    }
                }
            }
        }
        Set<String> se =new HashSet<String>(words);
        words.clear();
        words = new ArrayList<String>(se);
        for (String ls : words){
        	System.out.println(user+" "+ls);
        	System.out.println(user.indexOf(ls));
        	System.out.println(ls.indexOf(user));
        	System.out.println(res);
//        	if(user.toLowerCase()..indexOf(ls.toLowerCase())==-1){
//        		
//        		res = true;
//        	}
//        	else{
//        		res= false;
//        	}
//  }
		
        } 
		return res;
		
	}
	}
