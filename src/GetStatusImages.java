import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

public class GetStatusImages {

//	private static final String CONSUMER_KEY		= "ZZrkSzI0zqhgdbEbtdVaLTq9B";
//	private static final String CONSUMER_SECRET 	= "y1QhBhNLPJjZQCBDD2KTtIxMnKfG0GZxHXiNYB6l76BPonNCRk";
//
//	
//	private static final int TWEETS_PER_QUERY		= 100;
//
//	
//	private static final int MAX_QUERIES			= 5;
//
//	public static OAuth2Token getOAuth2Token()
//	{
//		OAuth2Token token = null;
//		ConfigurationBuilder cb;
//
//		cb = new ConfigurationBuilder();
//		cb.setApplicationOnlyAuthEnabled(true);
//
//		cb.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET);
//		cb.setHttpProxyHost("10.185.190.10");
//	    cb.setHttpProxyPort(8080);
//		try
//		{
//			token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
//		}
//		catch (Exception e)
//		{
//			System.out.println("Could not get OAuth2 token");
//			e.printStackTrace();
//			System.exit(0);
//		}
//
//		return token;
//	}
//
//	
//	public static Twitter getTwitter()
//	{
//		OAuth2Token token;
//
//		
//		token = getOAuth2Token();
//
//		
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//
//		cb.setApplicationOnlyAuthEnabled(true);
//
//		cb.setOAuthConsumerKey(CONSUMER_KEY);
//		cb.setOAuthConsumerSecret(CONSUMER_SECRET);
//		cb.setHttpProxyHost("10.185.190.10");
//	    cb.setHttpProxyPort(8080);
//		cb.setOAuth2TokenType(token.getTokenType());
//		cb.setOAuth2AccessToken(token.getAccessToken());
//
//		
//		return new TwitterFactory(cb.build()).getInstance();
//
//	}
//	public static void main(String[] a) throws NumberFormatException, TwitterException, SQLException {
//		
//		Connection conn = MySingleTon.getInstance();
//		Statement stmt = conn.createStatement();
//		List<String> mediaids=new ArrayList<>();
//		String uq="select * from tweetinfocopy1 ";
//		ResultSet uqrs=stmt.executeQuery(uq);
//		String id="";
//		String keyword="";
//		String txt="";
//		while(uqrs.next()){
//		keyword=uqrs.getString("keyword");
//		id=uqrs.getString("id");
//		txt=uqrs.getString("msg");
//		if(StringUtils.contains(txt, "https://")||StringUtils.contains(txt, "https://")&& !StringUtils.startsWith(txt, "RT")){
//			
//			
//			mediaids.add(id);
//		}
//		}
//		mediaanalysis(mediaids,keyword);
//	
//		
//		
//}
	public static void mediaanalysis(Status s,String Keyword) throws NumberFormatException, TwitterException{
		//Twitter twitter = getTwitter();
		//System.out.println(ids.size());
	//	for(TweetInfo item:ids){
	//	System.out.println(ids.get(i));
	//	Status status = twitter.showStatus((item.getID()));
		try{
		
		MediaEntity[] me=s.getMediaEntities();
		if(me.length>0){
		for (MediaEntity mediaEntity : me) {
			  if(mediaEntity.getType().equalsIgnoreCase("photo")){
			//	  System.out.println(ID);
	       System.out.println(mediaEntity.getType() + ": " + mediaEntity.getMediaURL());
	       String filename="C:\\Users\\ghfiy\\NewPhotos\\"+Keyword+s.getId()+".jpg";
	       ImageTest it=new ImageTest();
	       it.image(mediaEntity.getMediaURL(), filename);

		}
		}
		}
		}
		catch(Exception e){
					
		}
	}
}

