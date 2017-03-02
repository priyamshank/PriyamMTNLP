# Import the pandas package, then use the "read_csv" function to read
# the labeled training data
import csv

import sklearn
from sklearn.metrics import classification_report, f1_score, accuracy_score, confusion_matrix
import pandas as pd
import re
import nltk
import matplotlib.pyplot as plt


from nltk.corpus import stopwords


train = pd.read_csv('traindataagri.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["Category", "Tweet"])
print train
def review_to_words(raw_text):
    # Function to convert a raw review to a string of words
    # The input is a single string (a raw movie review), and
    # the output is a single string (a preprocessed movie review)
    #
    # 1. Remove HTML
    # review_text = BeautifulSoup(raw_review).get_text()
    #
    # 2. Remove non-letters
    letters_only = re.sub("[^a-zA-Z]", " ", raw_text)
    #
    # 3. Convert to lower case, split into individual words
    words = letters_only.lower().split()
    #
    # 4. In Python, searching a set is much faster than searching
    #   a list, so convert the stop words to a set
    print stopwords.words("english")
    words = [w for w in words if not w in stopwords.words("english")]
    print words
    stops = set(stopwords.words("english"))
    #
    # 5. Remove stop words
    meaningful_words = [w for w in words if not w in stops]
    #
    # 6. Join the words back into one string separated by space,
    # and return the result.
    return( " ".join( meaningful_words ))

# Get the number of reviews based on the dataframe column size
num_reviews = train["Tweet"].size

# Initialize an empty list to hold the clean reviews
clean_train_reviews = []

# Loop over each review; create an index i that goes from 0 to the length
# of the movie review list
for i in xrange( 0, num_reviews ):
    # Call our function for each one, and add the result to the list of
    # clean reviews
    clean_train_reviews.append( review_to_words( train["Tweet"][i] ) )

print "Creating the bag of words...\n"
from sklearn.feature_extraction.text import CountVectorizer

# Initialize the "CountVectorizer" object, which is scikit-learn's
# bag of words tool.
vectorizer = CountVectorizer(analyzer = "word",   \
                             tokenizer = None,    \
                             preprocessor = None, \
                             stop_words = None,   \
                             max_features = 5000)

# fit_transform() does two functions: First, it fits the model
# and learns the vocabulary; second, it transforms our training data
# into feature vectors. The input to fit_transform should be a list of
# strings.
train_data_features = vectorizer.fit_transform(clean_train_reviews)

# Numpy arrays are easy to work with, so convert the result to an
# array
train_data_features = train_data_features.toarray()

# Take a look at the words in the vocabulary
vocab = vectorizer.get_feature_names()
print vocab

import numpy as np

# Sum up the counts of each vocabulary word
dist = np.sum(train_data_features, axis=0)

# For each, print the vocabulary word and the number of times it
# appears in the training set
for tag, count in zip(vocab, dist):
    print count, tag

print "Training the random forest..."
from sklearn.ensemble import RandomForestClassifier

# Initialize a Random Forest classifier with 100 trees
forest = RandomForestClassifier(n_estimators = 100)

# Fit the forest to the training set, using the bag of words as
# features and the sentiment labels as the response variable
#
# This may take a few minutes to run
forest = forest.fit( train_data_features, train["Category"] )

# Read the test data
# test = pd.read_csv("testdata.txt",  delimiter="\t",encoding="utf-8")
test = pd.read_csv('testdataagri.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["Category", "Tweet"])
# Verify that there are 25,000 rows and 2 columns
print test.shape

# Create an empty list and append the clean reviews one by one
num_reviews = len(test["Tweet"])
clean_test_reviews = []

print "Cleaning and parsing the test set movie reviews...\n"
for i in xrange(0,num_reviews):
    if( (i+1) % 1000 == 0 ):
        print "Review %d of %d\n" % (i+1, num_reviews)
    clean_review = review_to_words( test["Tweet"][i] )
    clean_test_reviews.append( clean_review )

# Get a bag of words for the test set, and convert to a numpy array
test_data_features = vectorizer.transform(clean_test_reviews)
test_data_features = test_data_features.toarray()

# Use the random forest to make sentiment label predictions
result = forest.predict(test_data_features)
def measure_performance(X, y, clf, show_accuracy=True, show_classification_report=True, show_confusion_matrix=True):
    y_pred = clf.predict(X)
    if show_accuracy:
        print ("Accuracy:{0:.3f}".format(sklearn.metrics.accuracy_score(y, y_pred)), "\n")

    if show_classification_report:
        print ("Classification report")
        print (sklearn.metrics.classification_report(y, y_pred))

    if show_confusion_matrix:
        print ("Confusion matrix")
        print (sklearn.metrics.confusion_matrix(y, y_pred), "\n")

measure_performance(test_data_features,test["Category"],forest)
conf = sklearn.metrics.confusion_matrix(test["Category"], result)

plt.imshow(conf, cmap='Accent', interpolation='nearest')
plt.colorbar()
# plt.imshow(np.random.random((5,5)), interpolation='nearest')
# plt.xticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
# plt.yticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
# plt.show()
plt.xticks(np.arange(0,2), ['no', 'yes'])
plt.yticks(np.arange(0,2), ['no', 'yes'])
plt.show()
# Copy the results to a pandas dataframe with an "id" column and
# a "sentiment" column
output = pd.DataFrame( data={"Tweet":test["Tweet"],"Category":result} )

# Use pandas to write the comma-separated output file
output.to_csv( "Bag_of_Words_model.csv", index=False )