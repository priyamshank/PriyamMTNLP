import matplotlib.pyplot as plt
import csv
from textblob import TextBlob
import pandas
from nltk.tokenize import RegexpTokenizer
import sklearn
import cPickle
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.svm import SVC, LinearSVC
from sklearn.metrics import classification_report, f1_score, accuracy_score, confusion_matrix
from sklearn.pipeline import Pipeline
from sklearn.grid_search import GridSearchCV
from sklearn.cross_validation import StratifiedKFold, cross_val_score, train_test_split
from sklearn import tree
from sklearn.tree import DecisionTreeClassifier
from sklearn.learning_curve import learning_curve


# from sklearn.cross_validation import StratifiedKFold
#
# eval_size = 0.10
# kf = StratifiedKFold(y, round(1. / eval_size))
# train_indices,valid_indices=next(iter(kf))
# X_train, y_train = X[train_indices], y[train_indices]
# X_valid, y_valid = X[valid_indices], y[valid_indices]
messages = pandas.read_csv('CompleteTraningFiletext.txt', sep='\t', quoting=csv.QUOTE_NONE)

row = next(messages)
feature_names = np.array(row)
# print messages
# print(messages.groupby('label').describe())

titanic_X, titanic_y = [], []
for row in messages:
        titanic_X.append(row)
        titanic_y.append(row[7])  # The target value is "AgriTweet"

titanic_X = np.array(titanic_X)
titanic_y = np.array(titanic_y)
print (feature_names, titanic_X[0], titanic_y[0])


def split_into_tokens(message):
    message = unicode(message, 'utf8')  # convert bytes into proper unicode
    return TextBlob(message).words


messages.message.head().apply(split_into_tokens)


def split_into_lemmas(message):
    message = unicode(message, 'utf8').lower()
    words = TextBlob(message).words
    # for each word, take its "base form" = lemma
    return [word.lemma for word in words]
