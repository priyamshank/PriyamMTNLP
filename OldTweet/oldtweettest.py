import got

tweetCriteria = got.manager.TweetCriteria().setQuerySearch('"yellow rust"').setSince("2016-03-01").setUntil(
    "2016-06-30")
tweet = got.manager.TweetManager.getTweets(tweetCriteria)[0]
thefile = open('output_got.csv', 'w')

for item in tweet:
    thefile.write("%s\n" % item)
