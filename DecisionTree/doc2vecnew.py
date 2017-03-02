from sklearn.metrics import classification_report

from ReadACleanT import clean_tweet
import pandas as pd
import random
import csv
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import confusion_matrix
import statsmodels.api as sm

df = pd.read_csv('BookCategory.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["label", "text"])

df.loc[:, 'text'] = df.loc[:, 'text'].map(clean_tweet)
print df
udf = pd.read_csv('CompleteTestValidation.txt', sep='\t', quoting=csv.QUOTE_NONE,
                            names=["Keyword", "Username","date","retweets","text","hashtags","id"])
udf = udf[[u'text']]
udf.loc[:,'text'] = udf.loc[:,'text'].map(clean_tweet)
print udf
# from gensim.models.doc2vec import TaggedDocument, Doc2Vec
#
# TotalNum = df.size / 2
# TotalNum_Unlabed = udf.size
# TestNum = 3000
# TrainNum = TotalNum - TestNum
# # print TotalNum, TotalNum_Unlabed, TestNum, TrainNum
# documents = [TaggedDocument(list(df.loc[i, 'text']), [i]) for i in range(0, TotalNum)]
# # print documents
# documents_unlabeled = [TaggedDocument(list(udf.loc[i, 'text']), [i + TotalNum]) for i in range(0, TotalNum_Unlabed)]
# # print documents_unlabeled
# documents_all = documents + documents_unlabeled
# # print documents_all
# Doc2VecTrainID = range(0, TotalNum + TotalNum_Unlabed)
# print Doc2VecTrainID
# random.shuffle(Doc2VecTrainID)
# trainDoc = [documents_all[id] for id in Doc2VecTrainID]
# # print trainDoc
# Labels = df.loc[:, 'label']
# import multiprocessing
#
# cores = multiprocessing.cpu_count()
# model_DM = Doc2Vec(size=400, window=8, min_count=1, sample=1e-4, negative=5, workers=cores, dm=1, dm_concat=1)
# model_DBOW = Doc2Vec(size=400, window=8, min_count=1, sample=1e-4, negative=5, workers=cores, dm=0)
#
# model_DM.build_vocab(trainDoc)
# model_DBOW.build_vocab(trainDoc)
#
# for it in range(0, 10):
#     random.shuffle(Doc2VecTrainID)
#     trainDoc = [documents_all[id] for id in Doc2VecTrainID]
#     model_DM.train(trainDoc)
#     model_DBOW.train(trainDoc)
#
#
# random.seed(1212)
# newindex = random.sample(range(0,TotalNum),TotalNum)
# testID = newindex[-TestNum:]
# trainID = newindex[:-TestNum]
# train_targets, train_regressors = zip(*[(Labels[id], list(model_DM.docvecs[id])+list(model_DBOW.docvecs[id])) for id in trainID])
# train_regressors = sm.add_constant(train_regressors)
# predictor = LogisticRegression(multi_class='multinomial',solver='lbfgs')
# predictor.fit(train_regressors,train_targets)
#
# accus = []
# Gmeans = []
# accu = 0
# test_regressors = [list(model_DM.docvecs[id]) + list(model_DBOW.docvecs[id]) for id in testID]
# test_regressors = sm.add_constant(test_regressors)
# test_predictions = predictor.predict(test_regressors)
# print test_predictions
# for i in range(0, TestNum):
#     if test_predictions[i] == df.loc[testID[i], u'label']:
#         accu = accu + 1
# accus = accus + [1.0 * accu / TestNum]
# # print accus
# confusionM = confusion_matrix(test_predictions, (df.loc[testID, u'label']))
# print confusionM
# print classification_report(test_predictions,(df.loc[testID, u'label']))
# Gmeans = Gmeans + [pow(((1.0 * confusionM[0, 0] / (confusionM[1, 0] + confusionM[2, 0] + confusionM[0, 0])) * (
# 1.0 * confusionM[1, 1] / (confusionM[1, 1] + confusionM[2, 1] + confusionM[0, 1])) * (
#                         1.0 * confusionM[2, 2] / (confusionM[1, 2] + confusionM[2, 2] + confusionM[0, 2]))), 1.0 / 3)]
# train_predictions = predictor.predict(train_regressors)
# print Gmeans
#
# for i in range(0, len(train_targets)):
#     if train_predictions[i] == train_targets[i]:
#         accu = accu + 1
# accus = accus + [1.0 * accu / len(train_targets)]
# # print accus
# confusionM = confusion_matrix(train_predictions, train_targets)
# print confusionM
# Gmeans = Gmeans + [pow(((1.0 * confusionM[0, 0] / (confusionM[1, 0] + confusionM[2, 0] + confusionM[0, 0])) * (
# 1.0 * confusionM[1, 1] / (confusionM[1, 1] + confusionM[2, 1] + confusionM[0, 1])) * (
#                         1.0 * confusionM[2, 2] / (confusionM[1, 2] + confusionM[2, 2] + confusionM[0, 2]))), 1.0 / 3)]
# print Gmeans