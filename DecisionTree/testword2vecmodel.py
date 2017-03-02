import gensim
from gensim.models import Word2Vec

model = gensim.models.KeyedVectors.load_word2vec_format('w2v.txt', binary=False)
print model.most_similar('fusarium')