import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Twitter;

public class FileCreation {

	public static void main(String[] args) throws SQLException, UnsupportedEncodingException, FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		Connection conn = MySingleTon.getInstance();
		Statement stmt = conn.createStatement();
	
		 String uq="select id,msg,keyword from cstweets where (lower(keyword) like lower('%rust') or "+
                                             " lower(keyword) like lower('%Powdery mildew%') or "+
                                             " lower(keyword) like lower('%root%') or "+
                                             " lower(keyword) like lower('%rot%') or "+
                                             " lower(keyword) like lower('%fusarium%') or "+
                                             " lower(keyword) like lower('%Anthracnose%') or"+
                                             " lower(keyword) like lower('%blight%') or "+
                                             " lower(keyword) like lower('%scurf%') or "+
                                             " lower(keyword) like lower('%septoria%') or "+
                                             " lower(keyword) like lower('%leaf spot%') or "+
                                             " lower(keyword) like lower('%scab%') or "+
                                             " lower(keyword) like lower('%sigatoka%') or "+
                                 			 " lower(keyword) like lower('%Downy mildew%')) "+
                                             " and msg not like '%RT%'";
			ResultSet uqrs=stmt.executeQuery(uq);
			String msg="";
			String id="";
			while(uqrs.next()){
			msg=uqrs.getString("msg");
			msg=removeUrl(msg);
			msg = msg.replaceAll("[^a-zA-Z0-9]", " ");
			msg = msg.replaceAll("@\\w+|#\\w+|\\bRT\\b", " ")
	                .replaceAll("\n", " ")
	                .replaceAll("[^\\p{L}\\p{N} ]+", " ")
	                .replaceAll(" +", " ")
	                .trim();
			msg=msg.toLowerCase();
			System.out.println(msg);
			id=uqrs.getString("id");
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("C:/Mallet/td1/"+id+".txt"), "utf-8"))) {
		   writer.write(msg);
		}
			}
		
	}
	private static String removeUrl(String commentstr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentstr;
    }

}
