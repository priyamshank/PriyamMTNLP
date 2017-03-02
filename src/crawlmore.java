import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.jhenrique.manager.TweetManager;
import me.jhenrique.manager.TwitterCriteria;
import me.jhenrique.model.Tweet;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class crawlmore {

	private static final String[] SEARCH_TERM			= {"Fusarium"};
	public static void main(String[] args) throws SQLException, TwitterException {
		
		TwitterCriteria criteria = TwitterCriteria.create().setQuerySearch("fusarium")
              
                .setSince("2015-09-10")
                .setUntil("2015-09-12")
                .setMaxTweets(1);
    Tweet t = TweetManager.getTweets(criteria).get(0);
    System.out.println(t.getText());
//		// TODO Auto-generated method stub
//		 ConfigurationBuilder cb = new ConfigurationBuilder();
//	        cb.setDebugEnabled(true);
//	        cb.setOAuthConsumerKey("ZZrkSzI0zqhgdbEbtdVaLTq9B");
//	        cb.setOAuthConsumerSecret("y1QhBhNLPJjZQCBDD2KTtIxMnKfG0GZxHXiNYB6l76BPonNCRk");
//	        cb.setOAuthAccessToken("728932490082160640-0e4mDDjpHPXw9m1N7O6Nh4tTK2DDFaO");
//	        cb.setOAuthAccessTokenSecret("JeypBJhPqNc2cyRdMYc0Wbvp2E0yBwDHJInaAt9klLuGn");
//	       cb.setHttpProxyHost("10.185.190.10");
//	       cb.setHttpProxyPort(8080);
//	       TwitterFactory tf = new TwitterFactory(cb.build());
//	       Twitter twitter = tf.getInstance();
//
//
//
//List<Status> tweets = new ArrayList<Status>();
//
//DataTransfer dt=new DataTransfer();
//int wantedTweets = 300;
//long lastSearchID = Long.MAX_VALUE;
//int remainingTweets = wantedTweets;
////String keyword="fusarium";
//for(int termnum=0;termnum<SEARCH_TERM.length;termnum++){
//	System.out.println("//////////////////////////////////loop entered for "+SEARCH_TERM[termnum]+"////////////////////////////////////////");
//	Query query = new Query(SEARCH_TERM[termnum]);			
////Query query = new Query(keyword);
//  
//
//  while(remainingTweets > 0)
//  {
//    remainingTweets = wantedTweets - tweets.size();
//    if(remainingTweets > 100)
//    {
//      query.count(100);
//    }
//    else
//    {
//     query.count(remainingTweets); 
//    }
//    QueryResult result;
//    result = twitter.search(query);
//    tweets.addAll(result.getTweets());
//    Status s = tweets.get(tweets.size()-1);
//    long firstQueryID = s.getId();
//    query.setMaxId(firstQueryID);
//    remainingTweets = wantedTweets - tweets.size();
//  }
//
//  System.out.println("result obtained "+tweets.size());
//  for (Status tweet : tweets) {
//      System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + tweet.getLang() + tweet.getCreatedAt());
//   //   dt.insertdata(SEARCH_TERM[termnum], tweet.getText(), tweet.getUser().getScreenName(), tweet.getCreatedAt().toString());
//  }
//}

	}


		  
	}


