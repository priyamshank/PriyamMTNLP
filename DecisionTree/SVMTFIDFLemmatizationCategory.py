import matplotlib.pyplot as plt
import csv
from textblob import TextBlob
import pandas
import sklearn
import cPickle
import numpy as np
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.svm import SVC, LinearSVC
from sklearn.metrics import classification_report, f1_score, accuracy_score, confusion_matrix
from sklearn.pipeline import Pipeline
from sklearn.grid_search import GridSearchCV
from sklearn.cross_validation import StratifiedKFold, cross_val_score, train_test_split
from sklearn.learning_curve import learning_curve
import preprocessor as p
import re
import sys

reload(sys)
sys.setdefaultencoding("utf-8")
# messages = [line.rstrip() for line in open('td.txt')]
# print(len(messages))
dataframe = pandas.read_csv('BookCategory.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["label", "message"])

# print messages
# print(messages.groupby('label').describe())


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


dataframe['message'] = dataframe['message'].apply(preprocess_text)
print dataframe

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

pipeline_svm = Pipeline([
    ('bow', CountVectorizer(analyzer=split_into_lemmas)),
    ('tfidf', TfidfTransformer()),
    ('classifier', SVC()),  # <== change here
])


scores = cross_val_score(pipeline_svm,  # steps to convert raw messages into models
                         msg_train,  # training data
                         label_train,  # training labels
                         cv=10,  # split data randomly into 10 parts: 9 for training, 1 for scoring
                         scoring='accuracy',  # which scoring metric?
                         )
print scores

print scores.mean(), scores.std()


plt = plot_learning_curve(pipeline_svm, "accuracy vs. training set size", msg_train, label_train, cv=10)
plt.show()
# pipeline parameters to automatically explore and tune
param_svm = [
  {'classifier__C': [1, 10, 100, 1000], 'classifier__kernel': ['linear']},
  {'classifier__C': [1, 10, 100, 1000], 'classifier__gamma': [0.001, 0.0001], 'classifier__kernel': ['rbf']},
]

grid_svm = GridSearchCV(
    pipeline_svm,  # pipeline from above
    param_grid=param_svm,  # parameters to tune via cross validation
    refit=True,  # fit using all data, on the best detected classifier
    scoring='accuracy',  # what score are we optimizing?
    cv=StratifiedKFold(label_train, n_folds=5),  # what type of cross validation to use
)

svm_detector = grid_svm.fit(msg_train, label_train) # find the best combination from param_svm
print svm_detector.grid_scores_

print confusion_matrix(label_test, svm_detector.predict(msg_test))
print classification_report(label_test, svm_detector.predict(msg_test))
conf1 = sklearn.metrics.confusion_matrix(label_test, svm_detector.predict(msg_test))
plt.imshow(conf1, cmap='Accent', interpolation='nearest')
plt.colorbar()
# plt.imshow(np.random.random((5,5)), interpolation='nearest')
plt.xticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
plt.yticks(np.arange(0,6), ['Ad', 'Awareness', 'Research', 'News', 'Others', 'Self'])
plt.show()
with open('agriclass.pkl', 'wb') as fout:
    cPickle.dump(svm_detector, fout)

# ...and load it back, whenever needed, possibly on a different machine
# svm_detector_reloaded = cPickle.load(open('category_detector.pkl'))