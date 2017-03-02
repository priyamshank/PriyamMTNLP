from tokenize import tokenize

import gensim
from gensim.models import Doc2Vec
from tabulate import tabulate
import matplotlib.pyplot as plt
import seaborn as sns
import pandas
import numpy as np
from gensim.models.word2vec import Word2Vec
from gensim import models
from collections import Counter, defaultdict
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.ensemble import ExtraTreesClassifier
from sklearn.naive_bayes import BernoulliNB, MultinomialNB
from sklearn.pipeline import Pipeline
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score
from sklearn.cross_validation import cross_val_score, train_test_split
from sklearn.cross_validation import StratifiedShuffleSplit
import sys
import csv
import re
from textblob import TextBlob
import preprocessor as p
import copy

reload(sys)
sys.setdefaultencoding("utf-8")
# messages = [line.rstrip() for line in open('td.txt')]
# print(len(messages))
dataframe = pandas.read_csv('Bookcategory.txt', sep='\t', quoting=csv.QUOTE_NONE,
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
X, y = np.array(dataframe['message']), np.array(dataframe['label'])
print X
np.savetxt("test1.txt", np.c_[y,X],fmt="%s", delimiter="\t" )
# min_count = 2
# size = 50
# window = 4
# model = gensim.models.Word2Vec(X, size=100)
# w2v = dict(zip(model.index2word, model.syn0))

corpus = list(dataframe['message'])
print corpus
# corpus = corpus.map(lambda x: tokenize(x))
model = Word2Vec(corpus, size=100, window=5, min_count=5, workers=2)
model.save("vec")
# model.init_sims(replace=True)
# # # doc_model = copy.deepcopy(model)
# # # doc_model.__class__ = Doc2Vec
# # # model.index2word
# w2v = [model[word] for word in model.Vocab]
# # w2v = {w: vec for w, vec in zip(models.index2word, model.syn0)}
#
# mult_nb = Pipeline([("count_vectorizer", CountVectorizer(analyzer=lambda x: x)), ("multinomial nb", MultinomialNB())])
# bern_nb = Pipeline([("count_vectorizer", CountVectorizer(analyzer=lambda x: x)), ("bernoulli nb", BernoulliNB())])
# mult_nb_tfidf = Pipeline([("tfidf_vectorizer", TfidfVectorizer(analyzer=lambda x: x)), ("multinomial nb", MultinomialNB())])
# bern_nb_tfidf = Pipeline([("tfidf_vectorizer", TfidfVectorizer(analyzer=lambda x: x)), ("bernoulli nb", BernoulliNB())])
# # SVM - which is supposed to be more or less state of the art
# # http://www.cs.cornell.edu/people/tj/publications/joachims_98a.pdf
# svc = Pipeline([("count_vectorizer", CountVectorizer(analyzer=lambda x: x)), ("linear svc", SVC(kernel="linear"))])
# svc_tfidf = Pipeline([("tfidf_vectorizer", TfidfVectorizer(analyzer=lambda x: x)), ("linear svc", SVC(kernel="linear"))])
#
# class MeanEmbeddingVectorizer(object):
#     def __init__(self, word2vec):
#         self.word2vec = word2vec
#         # if a text is empty we should return a vector of zeros
#         # with the same dimensionality as all the other vectors
#         self.dim = len(word2vec.itervalues().next())
#
#     def fit(self, X, y):
#         return self
#
#     def transform(self, X):
#         return np.array([
#             np.mean([self.word2vec[w] for w in words if w in self.word2vec]
#                     or [np.zeros(self.dim)], axis=0)
#             for words in X
#         ])
#
# class TfidfEmbeddingVectorizer(object):
#     def __init__(self, word2vec):
#         self.word2vec = word2vec
#         self.word2weight = None
#         self.dim = len(word2vec.itervalues().next())
#
#     def fit(self, X, y):
#         tfidf = TfidfVectorizer(analyzer=lambda x: x)
#         tfidf.fit(X)
#         # if a word was never seen - it must be at least as infrequent
#         # as any of the known words - so the default idf is the max of
#         # known idf's
#         max_idf = max(tfidf.idf_)
#         self.word2weight = defaultdict(
#             lambda: max_idf,
#             [(w, tfidf.idf_[i]) for w, i in tfidf.vocabulary_.items()])
#
#         return self
#
#     def transform(self, X):
#         return np.array([
#                 np.mean([self.word2vec[w] * self.word2weight[w]
#                          for w in words if w in self.word2vec] or
#                         [np.zeros(self.dim)], axis=0)
#                 for words in X
#             ])
#
# etree_w2v = Pipeline([
#     ("word2vec vectorizer", MeanEmbeddingVectorizer(model)),
#     ("extra trees", ExtraTreesClassifier(n_estimators=200))])
# etree_w2v_tfidf = Pipeline([
#     ("word2vec vectorizer", TfidfEmbeddingVectorizer(model)),
#     ("extra trees", ExtraTreesClassifier(n_estimators=200))])
# all_models = [
#     ("mult_nb", mult_nb),
#     ("mult_nb_tfidf", mult_nb_tfidf),
#     ("bern_nb", bern_nb),
#     ("bern_nb_tfidf", bern_nb_tfidf),
#     ("svc", svc),
#     ("svc_tfidf", svc_tfidf),
#     ("w2v", etree_w2v),
#     ("w2v_tfidf", etree_w2v_tfidf),
# ]
# scores = sorted([(name, cross_val_score(model, msg_train, label_train, cv=5).mean())
#                  for name, model in all_models],
#                 key=lambda (_, x): -x)
# print tabulate(scores, floatfmt=".4f", headers=("model", 'score'))
#
# plt.figure(figsize=(15, 6))
# sns.barplot(x=[name for name, _ in scores], y=[score for _, score in scores])