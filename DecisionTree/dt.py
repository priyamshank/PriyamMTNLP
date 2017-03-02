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
import pydotplus
import preprocessor as p
from ttp import ttp
from ttp import utils
import re
import sys

reload(sys)
sys.setdefaultencoding("utf-8")
# messages = [line.rstrip() for line in open('td.txt')]
# print(len(messages))
dataframe = pandas.read_csv('Bookcategory.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["label", "message"])


print(dataframe.groupby('label').describe())


def preprocess_text(message):
    for i, row in dataframe.iterrows():

        # print row['message']
        # clean_tweet = re.match('(.*?)http.*?\s?(.*?)', message)


            message = p.tokenize(message)
            pattern = re.compile("[^\w$ ]")
            message = pattern.sub('', message)
            message = re.sub('[0-9]+', '', message)
            message = message.replace('$PIC$', '$PIC$ ')
            message = message.replace('$NUMBER$', '$NUMBER$ ')
            message = message.replace('$URL$', '$URL$ ')
            if message.__contains__('$PIC$')|message.__contains__('$URL'):
               message = message.rsplit('$',1)[0]+"$"
            message = re.sub(r'(?<=[a-z])(?=[A-Z])', ' ', message)
            print message
            return message


# dataframe['message'] = dataframe['message'].apply(preprocess_text)
# print dataframe

def split_into_tokens(message):
    message = message.decode().encode('utf-8')  # convert bytes into proper unicode
    return TextBlob(message).words


# dataframe.message.head().apply(split_into_tokens)


def split_into_lemmas(message):
    message = message.encode("utf-8").lower()
    words = TextBlob(message).words
    # for each word, take its "base form" = lemma
    return [word.lemma for word in words]

# messages.message.head().apply(split_into_lemmas)
# bow_transformer = CountVectorizer(analyzer=split_into_lemmas).fit(messages['message'])
# print len(bow_transformer.vocabulary_)
# messages_bow = bow_transformer.transform(messages['message'])
# print 'sparse matrix shape:', messages_bow.shape
# print 'number of non-zeros:', messages_bow.nnz
# print 'sparsity: %.2f%%' % (100.0 * messages_bow.nnz / (row['mesmmessages_bow.shape[0] * messages_bow.shape[1]))
# tfidf_transformer = TfidfTransformer().fit(messages_bow)
# messages_tfidf = tfidf_transformer.transform(messages_bow)
# print messages_tfidf.shape
# spam_detector = MultinomialNB().fit(messages_tfidf, messages['label'])
# all_predictions = spam_detector.predict(messages_tfidf)
# print all_predictions
# print 'accuracy', accuracy_score(messages['label'], all_predictions)
# print 'confusion matrix\n', confusion_matrix(messages['label'], all_predictions)
# print '(row=expected, col=predicted)'
# plt.matshow(confusion_matrix(messages['label'], all_predictions), cmap=plt.cm.binary, interpolation='nearest')
# plt.title('confusion matrix')
# plt.colorbar()
# plt.ylabel('expected label')
# plt.xlabel('predicted label')
# plt.show()
# print classification_report(messages['label'], all_predictions)

msg_train, msg_test, label_train, label_test = \
    train_test_split(dataframe['message'], dataframe['label'], test_size=0.2)

print len(msg_train), len(msg_test), len(msg_train) + len(msg_test)

# from sklearn.feature_extraction.text import TfidfVectorizer
# from sklearn.feature_extraction.text import ENGLISH_STOP_WORDS
#
# # add some tweets specific stop words to the built-in english list
# remove = ['amp', 'cc', 'did', 'don', 'rt', 'll', 'oh', 've', 'yes', 'let', 'going', 'via', 're', 'tweet']
# stop = list(ENGLISH_STOP_WORDS) + remove
#
# import re
# from nltk.stem.snowball import *
#
# stemmer = SnowballStemmer('english')
#
#
# class NoUrls_TfidfVectorizer(TfidfVectorizer):
#     def build_preprocessor(self):
#         url_pattern = re.compile(r'http(s?)://[\w./]+')
#         pic_pattern = re.compile(r'pic.twitter.com/[\w.]+')
#         preprocessor = super(NoUrls_TfidfVectorizer, self).build_preprocessor()
#         return lambda doc: (pic_pattern.sub('', url_pattern.sub('', preprocessor(doc))))
#
#
# class NoUrls_Stemmed_TfidfVectorizer(TfidfVectorizer):
#     def build_preprocessor(self):
#         url_pattern = re.compile(r'http(s?)://[\w./]+')
#         pic_pattern = re.compile(r'pic.twitter.com/[\w.]+')
#         preprocessor = super(NoUrls_Stemmed_TfidfVectorizer, self).build_preprocessor()
#         return lambda doc: (pic_pattern.sub('', url_pattern.sub('', preprocessor(doc))))
#
#     def build_tokenizer(self):
#         tokenizer = super(NoUrls_Stemmed_TfidfVectorizer, self).build_tokenizer()
#         return lambda doc: (stemmer.stem(w) for w in tokenizer(doc))
#
#
# from sklearn.linear_model import LogisticRegression
# logistic_tfidf = NoUrls_TfidfVectorizer(min_df=1, stop_words=stop, strip_accents='unicode')
# logistic_classifier = LogisticRegression()
# logistic_pipeline = Pipeline([('tfidf', logistic_tfidf), ('clf', logistic_classifier)])
# logistic_classifier.get_params()
# parameters = {
#     'tfidf__max_df': (0.8, 1.0),
#     'tfidf__ngram_range': ((1, 1), (1, 2)),
#     'tfidf__norm': ('l1', 'l2'),
#     'clf__C': (1, 5, 7)
# }
#
# logistic_gs = GridSearchCV(logistic_pipeline, parameters, verbose=1, refit=False)
# logistic_gs.fit(msg_train, label_train)
#
# from sklearn.grid_search import GridSearchCV
# from sklearn import metrics
# from sklearn.metrics import classification_report
#
# '''helper function for displaying best found features on grid_search'''
#
#
# def print_grid_search_metrics(gs):
#     print("Best score: %0.3f" % gs.best_score_)
#     print("Best parameters set:")
#     best_parameters = gs.best_params_
#     for param_name in sorted(parameters.keys()):
#         print("\t%s: %r" % (param_name, best_parameters[param_name]))
#
#
# '''helper function for displaying the algorithm metrics'''
#
#
# def print_metrics(model_name, y_labels, y_predicted):
#     print "MODEL: " + model_name
#     print 'Test Accuracy: ' + str(metrics.accuracy_score(y_labels, y_predicted))
#
#     print '\nClassification report:'
#     print classification_report(y_labels, y_predicted)
#
#     print '\nConfusion matrix:'
#     print metrics.confusion_matrix(y_labels, y_predicted)
#
#
# '''helper to display the most informative features for each group'''
#
#
# def show_most_informative_features(vectorizer, clf, n=20):
#     feature_names = vectorizer.get_feature_names()
#     coefs_with_names = sorted(zip(clf.coef_[0], feature_names))
#     top_features = zip(coefs_with_names[:n], coefs_with_names[:-(n + 1):-1])  # top features for both groups
#     for (coef_1, fn_1), (coef_2, fn_2) in top_features:
#         print "\t%.4f\t%-15s\t\t%.4f\t%-15s" % (coef_1, fn_1, coef_2, fn_2)
#
# print_grid_search_metrics(logistic_gs)
# logistic_vect = NoUrls_TfidfVectorizer(ngram_range=(1, 1), min_df=1, max_df=0.8, norm='l2', stop_words=stop, strip_accents='unicode')
# logistic_classifier = LogisticRegression(C=5)
# logistic_pipeline = Pipeline([('tfidf', logistic_vect), ('clf', logistic_classifier)])
# score = cross_val_score(logistic_pipeline, msg_test, label_test, cv=10)
# print "10-fold cross validation accuracy: " + str(np.mean(score))
# predictive_model = logistic_pipeline.fit(msg_train, msg_train)
# y_logistic_predicted = logistic_pipeline.predict(msg_test)
#
# print_metrics("Logistic Regression", msg_test, y_logistic_predicted)


# X, y = np.array(msg_train), np.array(label_train)
# np.savetxt("traindataagri.txt", np.c_[y,X],fmt="%s", delimiter="\t" )
#
# X1, y1 = np.array(msg_test), np.array(label_test)
# np.savetxt("testdataagri.txt", np.c_[y1,X1],fmt="%s", delimiter="\t" )
#
#
# pipeline = Pipeline([
#     ('bow', CountVectorizer(analyzer=split_into_lemmas)),  # strings to token integer counts
#     ('tfidf', TfidfTransformer()),  # integer counts to weighted TF-IDF scores
#     ('classifier', MultinomialNB()),  # train on TF-IDF vectors w/ Naive Bayes classifier
# ])
# scores = cross_val_score(pipeline,  # steps to convert raw messages into models
#                          msg_train,  # training data
#                          label_train,  # training labels
#                          cv=10,  # split data randomly into 10 parts: 9 for training, 1 for scoring
#                          scoring='accuracy',  # which scoring metric?
#                          )
# print scores
#
# print scores.mean(), scores.std()
#
#
# def plot_learning_curve(estimator, title, X, y, ylim=None, cv=None,
#                         train_sizes=np.linspace(.1, 1.0, 5)):
#     plt.figure()
#     plt.title(title)
#     if ylim is not None:
#         plt.ylim(*ylim)
#     plt.xlabel("Training examples")
#     plt.ylabel("Score")
#     train_sizes, train_scores, test_scores = learning_curve(
#         estimator, X, y, cv=cv, train_sizes=train_sizes)
#     train_scores_mean = np.mean(train_scores, axis=1)
#     train_scores_std = np.std(train_scores, axis=1)
#     test_scores_mean = np.mean(test_scores, axis=1)
#     test_scores_std = np.std(test_scores, axis=1)
#     plt.grid()
#
#     plt.fill_between(train_sizes, train_scores_mean - train_scores_std,
#                      train_scores_mean + train_scores_std, alpha=0.1,
#                      color="r")
#     plt.fill_between(train_sizes, test_scores_mean - test_scores_std,
#                      test_scores_mean + test_scores_std, alpha=0.1, color="g")
#     plt.plot(train_sizes, train_scores_mean, 'o-', color="r",
#              label="Training score")
#     plt.plot(train_sizes, test_scores_mean, 'o-', color="g",
#              label="Cross-validation score")
#
#     plt.legend(loc="best")
#     return plt
#
#
# plt = plot_learning_curve(pipeline, "accuracy vs. training set size", msg_train, label_train, cv=5)
# plt.show()
# params = {
#     'tfidf__use_idf': (True, False),
#     'bow__analyzer': (split_into_lemmas, split_into_tokens),
# }
#
# grid = GridSearchCV(
#     pipeline,  # pipeline from above
#     params,  # parameters to tune via cross validation
#     refit=True,  # fit using all available data at the end, on the best found param combination
#     scoring='accuracy',  # what score are we optimizing?
#     cv=StratifiedKFold(label_train, n_folds=5),  # what type of cross validation to use
# )
# nb_detector = grid.fit(msg_train, label_train)
# print nb_detector.grid_scores_
# # print nb_detector.predict(["#Wedding / Special Occasion Wear ANN BALON Designer Chain Link Maxi Evening Dress http://goo.gl/hZHDGq "])[0]
# predictions = nb_detector.predict(msg_test)
# confusion_matrix(label_test, predictions)
# print classification_report(label_test, predictions)
# conf = sklearn.metrics.confusion_matrix(label_test, predictions)
# plt.imshow(conf, cmap='binary', interpolation='nearest')
# # plt.imshow(np.random.random((5,5)), interpolation='nearest')
# plt.xticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
# plt.yticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
# plt.show()
#
# pipeline_svm = Pipeline([
#     ('bow', CountVectorizer(analyzer=split_into_lemmas)),
#     ('tfidf', TfidfTransformer()),
#     ('classifier', SVC()),  # <== change here
# ])
#
# # pipeline parameters to automatically explore and tune
# param_svm = [
#   {'classifier__C': [1, 10, 100, 1000], 'classifier__kernel': ['linear']},
#   {'classifier__C': [1, 10, 100, 1000], 'classifier__gamma': [0.001, 0.0001], 'classifier__kernel': ['rbf']},
# ]
#
# grid_svm = GridSearchCV(
#     pipeline_svm,  # pipeline from above
#     param_grid=param_svm,  # parameters to tune via cross validation
#     refit=True,  # fit using all data, on the best detected classifier
#     scoring='accuracy',  # what score are we optimizing?
#     cv=StratifiedKFold(label_train, n_folds=5),  # what type of cross validation to use
# )
#
# svm_detector = grid_svm.fit(msg_train, label_train) # find the best combination from param_svm
# print svm_detector.grid_scores_
#
# print confusion_matrix(label_test, svm_detector.predict(msg_test))
# print classification_report(label_test, svm_detector.predict(msg_test))
# conf1 = sklearn.metrics.confusion_matrix(label_test, svm_detector.predict(msg_test))
# plt.imshow(conf1, cmap='rainbow', interpolation='nearest')
# plt.colorbar()
# # plt.imshow(np.random.random((5,5)), interpolation='nearest')
# plt.xticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
# plt.yticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
# plt.show()
# with open('category_detector.pkl', 'wb') as fout:
#     cPickle.dump(svm_detector, fout)
#
# # ...and load it back, whenever needed, possibly on a different machine
# svm_detector_reloaded = cPickle.load(open('category_detector.pkl'))