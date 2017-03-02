# -*- coding: utf-8 -*-

import pandas as pd

from nltk.tokenize import TweetTokenizer
from nltk.corpus import stopwords
import re, string
import nltk

# import regex
import re


# start process_tweet
def processTweet(tweet):
    # process the tweets


    # Convert to lower case
    tweet = tweet.lower()
    # Convert www.* or https?://* to URL
    tweet = re.sub('((www\.[^\s]+)|(https?://[^\s]+))', 'URL', tweet)
    # Convert @username to AT_USER
    tweet = re.sub('@[^\s]+', 'AT_USER', tweet)
    # Remove additional white spaces
    tweet = re.sub('[\s]+', ' ', tweet)
    # Replace #word with word
    tweet = re.sub(r'#([^\s]+)', r'\1', tweet)
    # trim
    tweet = tweet.strip('\'"')
    return tweet


# end

# Read the tweets one by one and process it
fp = open("C:/Users/ghfiy/Tweetmsgs.txt", 'r')
line = fp.readline()
print(line)

while line:
    processedTweet = processTweet(line)
    print(processedTweet)
    line = fp.readline()
# end loop
fp.close()

# df = pd.read_csv("C:/Users/ghfiy/twitterdata.csv", encoding="ISO-8859-1")
# print(df)
# tweets_texts = df["msg"].tolist()


# stopwords = stopwords.words('english')
# english_vocab = set(w.lower() for w in nltk.corpus.words.words())




# words = []
# for tw in tweets_texts:
#     words += process_tweet_text(tw)
# #print(words)
# print(words.__sizeof__())
