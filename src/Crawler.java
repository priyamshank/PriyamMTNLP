import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class Crawler {
    public static void main(String[] args) throws TwitterException {
    	
   
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("ZZrkSzI0zqhgdbEbtdVaLTq9B");
        cb.setOAuthConsumerSecret("y1QhBhNLPJjZQCBDD2KTtIxMnKfG0GZxHXiNYB6l76BPonNCRk");
        cb.setOAuthAccessToken("728932490082160640-0e4mDDjpHPXw9m1N7O6Nh4tTK2DDFaO");
        cb.setOAuthAccessTokenSecret("JeypBJhPqNc2cyRdMYc0Wbvp2E0yBwDHJInaAt9klLuGn");
       cb.setHttpProxyHost("10.185.190.10");
       cb.setHttpProxyPort(8080);
       
     TwitterFactory tf = new TwitterFactory(cb.build());
      Twitter twitter = tf.getInstance();
//            try {
//                Query query = new Query("fusarium");
//              //  query.getUntil("2012-02-20");
//            //  query.count(5000);
//                QueryResult result;
//                result = twitter.search(query);
//                List<Status> tweets = result.getTweets();
//                System.out.println("result obtained "+tweets.size());
//                for (Status tweet : tweets) {
//                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + tweet.getLang() + tweet.getCreatedAt());
//                }
//
//                System.exit(0);
//            } catch (TwitterException te) {
//                te.printStackTrace();
//                System.out.println("Failed to search tweets: " + te.getMessage());
//                System.exit(-1);
//            }
       Query query = new Query("fusarium");
       query.setCount(500);

       int searchResultCount;
       long lowestTweetId = Long.MAX_VALUE;

       do {
           QueryResult queryResult = twitter.search(query);

           searchResultCount = queryResult.getTweets().size();

           for (Status tweet : queryResult.getTweets()) {

               // do whatever with the tweet

               if (tweet.getId() < lowestTweetId) {
                   lowestTweetId = tweet.getId();
                   query.setMaxId(lowestTweetId);
               }
           }

       } while (searchResultCount != 0 && searchResultCount % 500 == 0);
}
}