import sys
import codecs
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.mlab import PCA
import gensim


def plotWords():
    # get model, we use w2v only
    w2v, d2v = gensim.models.Doc2Vec.load_word2vec_format("C:/Users/ghfiy/PycharmProjects/TwitterProcess/trained.word2vec")
    words_np=[]
    # a list of labels (words)
    words_label = []
    for word in w2v.vocab.keys():
        words_np.append(w2v[word])
        words_label.append(word)
    print('Added %s words. Shape %s' % (len(words_np), np.shape(words_np)))

    pca = PCA(n_components=2)
    pca.fit(words_np)
    reduced = pca.transform(words_np)

    # plt.plot(pca.explained_variance_ratio_)
    for index, vec in enumerate(reduced):
        # print ('%s %s'%(words_label[index],vec))
        if index < 100:
            x, y = vec[0], vec[1]
            plt.scatter(x, y)
            plt.annotate(words_label[index], xy=(x, y))
    plt.show()
    plt.plot()

plotWords()