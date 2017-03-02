import matplotlib.pyplot as plt
import csv
from textblob import TextBlob
import pandas
import sklearn
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import classification_report, f1_score, accuracy_score, confusion_matrix
from sklearn.pipeline import Pipeline
from sklearn.grid_search import GridSearchCV
from sklearn.cross_validation import StratifiedKFold, cross_val_score, train_test_split
from sklearn.learning_curve import learning_curve
import preprocessor as p
import re
import sys
from nltk.corpus import stopwords

reload(sys)
sys.setdefaultencoding("utf-8")

dataframe = pandas.read_csv('Bookcategory.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["label", "message"])

def preprocess_text(message):
    for i, row in dataframe.iterrows():
            message = p.tokenize(message)
            pattern = re.compile("[^\w$ ]")
            message = pattern.sub('', message)
            message = re.sub('[0-9]+', '', message)
            message = message.replace('$PIC$', '$PIC$ ')
            message = message.replace('$NUMBER$', '$NUMBER$ ')
            message = message.replace('$URL$', '$URL$ ')
            if message.__contains__('$PIC$')|message.__contains__('$URL'):
               message = message.rsplit('$',1)[0]+"$"
            # message = re.sub(r'(?<=[a-z])(?=[A-Z])', ' ', message)
            # letters_only = re.sub("[^a-zA-Z]", " ", message)
            #
            # 3. Convert to lower case, split into individual words
            words = message.lower().split()
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
            return (" ".join(meaningful_words))
            # print message
            # return message


dataframe['message'] = dataframe['message'].apply(preprocess_text)
print dataframe

def split_into_tokens(message):
    message = message.decode().encode('utf-8')  # convert bytes into proper unicode
    return TextBlob(message).words


def split_into_lemmas(message):
    message = message.encode("utf-8").lower()
    words = TextBlob(message).words
    # for each word, take its "base form" = lemma
    return [word.lemma for word in words]

msg_train, msg_test, label_train, label_test = \
    train_test_split(dataframe['message'], dataframe['label'], test_size=0.2)

print len(msg_train), len(msg_test), len(msg_train) + len(msg_test)
pipeline = Pipeline([
    ('bow', CountVectorizer(analyzer=split_into_lemmas)),  # strings to token integer counts
    ('tfidf', TfidfTransformer()),  # integer counts to weighted TF-IDF scores
    ('classifier', MultinomialNB()),  # train on TF-IDF vectors w/ Naive Bayes classifier
])
scores = cross_val_score(pipeline,  # steps to convert raw messages into models
                         msg_train,  # training data
                         label_train,  # training labels
                         cv=10,  # split data randomly into 10 parts: 9 for training, 1 for scoring
                         scoring='accuracy',  # which scoring metric?
                         )
print scores

print scores.mean(), scores.std()


def plot_learning_curve(estimator, title, X, y, ylim=None, cv=None,
                        train_sizes=np.linspace(.1, 1.0, 5)):
    plt.figure()
    plt.title(title)
    if ylim is not None:
        plt.ylim(*ylim)
    plt.xlabel("Training examples")
    plt.ylabel("Score")
    train_sizes, train_scores, test_scores = learning_curve(
        estimator, X, y, cv=cv, train_sizes=train_sizes)
    train_scores_mean = np.mean(train_scores, axis=1)
    train_scores_std = np.std(train_scores, axis=1)
    test_scores_mean = np.mean(test_scores, axis=1)
    test_scores_std = np.std(test_scores, axis=1)
    plt.grid()

    plt.fill_between(train_sizes, train_scores_mean - train_scores_std,
                     train_scores_mean + train_scores_std, alpha=0.1,
                     color="r")
    plt.fill_between(train_sizes, test_scores_mean - test_scores_std,
                     test_scores_mean + test_scores_std, alpha=0.1, color="g")
    plt.plot(train_sizes, train_scores_mean, 'o-', color="r",
             label="Training score")
    plt.plot(train_sizes, test_scores_mean, 'o-', color="g",
             label="Cross-validation score")

    plt.legend(loc="best")
    return plt


plt = plot_learning_curve(pipeline, "accuracy vs. training set size", msg_train, label_train, cv=5)
plt.show()
params = {
    'tfidf__use_idf': (True, False),
    'bow__analyzer': (split_into_lemmas, split_into_tokens),
}

grid = GridSearchCV(
    pipeline,  # pipeline from above
    params,  # parameters to tune via cross validation
    refit=True,  # fit using all available data at the end, on the best found param combination
    scoring='accuracy',  # what score are we optimizing?
    cv=StratifiedKFold(label_train, n_folds=5),  # what type of cross validation to use
)
nb_detector = grid.fit(msg_train, label_train)
print nb_detector.grid_scores_
# print nb_detector.predict(["#Wedding / Special Occasion Wear ANN BALON Designer Chain Link Maxi Evening Dress http://goo.gl/hZHDGq "])[0]
predictions = nb_detector.predict(msg_test)
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

measure_performance(msg_test,label_test,nb_detector)
confusion_matrix(label_test, predictions)
print classification_report(label_test, predictions)
conf = sklearn.metrics.confusion_matrix(label_test, predictions)
plt.imshow(conf, cmap='Accent', interpolation='nearest')
plt.colorbar()
# plt.imshow(np.random.random((5,5)), interpolation='nearest')
plt.xticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
plt.yticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
plt.show()