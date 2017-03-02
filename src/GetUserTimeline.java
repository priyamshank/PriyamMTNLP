import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import twitter4j.*;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.*;

public class GetUserTimeline{
	private static final String CONSUMER_KEY		= "ZZrkSzI0zqhgdbEbtdVaLTq9B";
	private static final String CONSUMER_SECRET 	= "y1QhBhNLPJjZQCBDD2KTtIxMnKfG0GZxHXiNYB6l76BPonNCRk";

	
	private static final int TWEETS_PER_QUERY		= 100;

	
	private static final int MAX_QUERIES			= 5;

	public static OAuth2Token getOAuth2Token()
	{
		OAuth2Token token = null;
		ConfigurationBuilder cb;

		cb = new ConfigurationBuilder();
		cb.setApplicationOnlyAuthEnabled(true);

		cb.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET);
		cb.setHttpProxyHost("10.185.190.10");
	    cb.setHttpProxyPort(8080);
		try
		{
			token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
		}
		catch (Exception e)
		{
			System.out.println("Could not get OAuth2 token");
			e.printStackTrace();
			System.exit(0);
		}

		return token;
	}

	
	public static Twitter getTwitter()
	{
		OAuth2Token token;

		
		token = getOAuth2Token();

		
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setApplicationOnlyAuthEnabled(true);

		cb.setOAuthConsumerKey(CONSUMER_KEY);
		cb.setOAuthConsumerSecret(CONSUMER_SECRET);
		cb.setHttpProxyHost("10.185.190.10");
	    cb.setHttpProxyPort(8080);
		cb.setOAuth2TokenType(token.getTokenType());
		cb.setOAuth2AccessToken(token.getAccessToken());

		
		return new TwitterFactory(cb.build()).getInstance();

	}
public static void main(String[] a) throws IOException, SQLException, TwitterException {
	
	Twitter twitter = getTwitter();

    int pageno = 1;
    Connection conn = MySingleTon.getInstance();
	Statement stmt = conn.createStatement();
	String uq="select tweetcreator,t.country from (select tweetcreator,unnest(countries) as country,date from tweetinfocopy1 t where date>'2016-10-31' and agriuser is not null) t where t.country like '%Canada%'";
	ResultSet uqrs=stmt.executeQuery(uq);
    String user1 = null;
    String user=null;
   List<String> agriuser=new ArrayList<>();
    while(uqrs.next()){
    	System.out.println("-------------------new user----------------------------");
    	user=uqrs.getString("tweetcreator");
    	System.out.println(user);
    	agriuser.add(user);
    }
    
    	 
    for(int i=0;i<agriuser.size();i++){
    	user1=agriuser.get(i);
    	List statuses = new ArrayList();
    	int size = statuses.size(); 
        Paging page = new Paging(pageno++, 50);
        System.out.println("getting timeline of user "+user1);
        statuses.addAll(twitter.getUserTimeline(user1));
       // statuses.addAll(twitter.getUserTimeline(user1, page));
        System.out.println("Total: "+statuses.size());
      
    
    ArrayList<String> arr = new ArrayList<String>();
    for(int i1=0;i1<statuses.size();i1++){
    	System.out.println("////////////////new status/////////////////");
    	String status=statuses.get(i1).toString();
    	String statusinter=StringUtils.substringAfter(status, "text=");
   
    	String statusfinal=StringUtils.substringBetween(statusinter, "'");
    	arr.add(statusfinal);
    	System.out.println(statusfinal);
 
    }
    String filename="C:\\Users\\ghfiy\\Agriuser\\"+user1+".txt";
    FileWriter writer = new FileWriter(filename); 
    for(String msg: arr) {
    	writer.write("\r\n");
    	msg=msg.replaceAll("http?://\\S+\\s?", "");
    	msg=msg.replaceAll("https?://\\S+\\s?", "");
		
		msg = msg.replaceAll("@\\w+|#\\w+|\\bRT\\b", "")
                .replaceAll("\n", " ")
                .replaceAll("[^\\p{L}\\p{N} ]+", " ")
                .replaceAll(" +", " ")
                .trim();
		msg=msg.toLowerCase();
      writer.write(msg);
    }
    writer.close();
    
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
