import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Test {

	ArrayList<String> words=new ArrayList<>();
	
//	public static void main(String[] args) throws IOException {
	public ArrayList<String> AgriconnectedTermExtraction() throws IOException{
		// TODO Auto-generated method stub
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
                for(String each : wordsArray){
                    if(!"".equals(each)){
                        words.add(each);
                    }
                }
            }
        }

//        for(String each : words){
//            System.out.println(each);
//        }
        Set<String> se =new HashSet<String>(words);
        words.clear();
        words = new ArrayList<String>(se);
//        for (Object ls : words){
//           System.out.println(ls);  }
//        	
//        	if(Text.contains(ls.toString())){
//        		System.out.println(Text+" related to agri because of keyword"+ ls.toString());
//        	}
//        }
        buf.close();
        return words;
    }
	
	
		
	}
	
	



