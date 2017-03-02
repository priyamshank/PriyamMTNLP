
import twitter4j.*;
import twitter4j.Query.ResultType;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.json.JSONException;

public class CrawlTest {

	
	private static final String CONSUMER_KEY		= "ZZrkSzI0zqhgdbEbtdVaLTq9B";
	private static final String CONSUMER_SECRET 	= "y1QhBhNLPJjZQCBDD2KTtIxMnKfG0GZxHXiNYB6l76BPonNCRk";
	private static final int TWEETS_PER_QUERY		= 100;
	private static final int MAX_QUERIES			= 5;

	public static String cleanText(String text)
	{
		text = text.replace("\n", "\\n");
		text = text.replace("\t", "\\t");

		return text;
	}

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

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, SQLException, JSONException, TwitterException
	{
		
		Connection conn = MySingleTon.getInstance();
		Statement stmt = conn.createStatement();
		Twitter twitter = getTwitter();
		List<TweetInfo> ti=new ArrayList<>();
		BufferedReader buf = new BufferedReader(new FileReader("Keywords"));
        ArrayList<String> SEARCH_TERM = new ArrayList<>();
        String lineJustFetched = null;
        String[] wordsArray;
        DataTransfer dt=new DataTransfer();

        while(true){
            lineJustFetched = buf.readLine();
            if(lineJustFetched == null){  
                break; 
            }else{
                wordsArray = lineJustFetched.split("\t");
                for(String each : wordsArray){
                    if(!"".equals(each)){
                    	SEARCH_TERM.add(each);
                    }
                }
            }
        }
	
		for(int i=0;i<SEARCH_TERM.size();i++){
		
			System.out.println("//////////////////////////////////loop entered for "+SEARCH_TERM.get(i)+"////////////////////////////////////////");
	
		int	totalTweets = 0;	
		long maxID = -1;
		
		
		try
		{
			
			Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
			RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
			System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
							  searchTweetsRateLimit.getRemaining(),
							  searchTweetsRateLimit.getLimit(),
							  searchTweetsRateLimit.getSecondsUntilReset());			
			for (int queryNumber=0;queryNumber < MAX_QUERIES; queryNumber++)
			{
				
				System.out.printf("\n\n!!! Starting loop %d\n\n", queryNumber);
				
				
			
				if (searchTweetsRateLimit.getRemaining() == 0)
				{
					
					System.out.printf("!!! Sleeping for %d seconds due to rate limits\n", searchTweetsRateLimit.getSecondsUntilReset());

					
					Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset()+2) * 1000l);
				}
				
				String term=SEARCH_TERM.get(i);
				term="\"" + term + "\"";
				Query q = new Query(term);			// Search for tweets that contains this term
				q.setCount(TWEETS_PER_QUERY);				// How many tweets, max, to retrieve
				q.resultType(ResultType.mixed);						// Get all tweets
				q.setLang("en");							// English language tweets, please
				String query="select max(date) as date from cstweets";
				ResultSet rs = stmt.executeQuery(query);
				Date d=null;
				 while(rs.next()){
				d=rs.getDate("date");
				 }
				q.setSince(d.toString());

				
				if (maxID != -1)
				{
					q.setMaxId(maxID - 1);
				}

			
				QueryResult r = twitter.search(q);
				
				if (r.getTweets().size() == 0)
				{
					break;			// Nothing? We must be done
				}


				
				for (Status s1: r.getTweets())				// Loop through all the tweets...
				{
				
					totalTweets++;

				
					if (maxID == -1 || s1.getId() < maxID)
						
					{
						maxID = s1.getId();
					}

					long ID = s1.getId();
					String keyword=SEARCH_TERM.get(i);
					String tweetmsg=s1.getText();
					String createdby=s1.getUser().getScreenName();
					Date createddate=s1.getCreatedAt();
					String userlocation=s1.getUser().getLocation();
					String userdesc=s1.getUser().getDescription();
					TweetInfo item = new TweetInfo(keyword,createdby,tweetmsg,userlocation,createddate,userdesc);
					ti.add(item);
//					GetStatusImages gsi=new GetStatusImages();
//					if(s1.isRetweet()==false)
//					gsi.mediaanalysis(s1, keyword);
				}
				
			}
			
			}

		
		catch (Exception e)
		{
			
			System.out.println("That didn't work well...wonder why?");

			e.printStackTrace();

		}

		System.out.printf("\n\nA total of %d tweets retrieved\n", totalTweets);
		
		
    }
		Tweetinfoprocessing tip=new Tweetinfoprocessing();		
		tip.tweetinfoprint(ti);
}
}