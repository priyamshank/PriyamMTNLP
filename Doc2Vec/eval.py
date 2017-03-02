import gensim

model=gensim.models.Doc2Vec.load_word2vec_format('trained.word2vec')
print(model.most_similar('septoria'))
print(model.docvecs.most_similar(str(3002)))
