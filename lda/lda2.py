from nltk.tokenize import RegexpTokenizer
from stop_words import get_stop_words
from nltk.stem.porter import PorterStemmer
import gensim
from gensim import corpora
import glob
import pyLDAvis.gensim as gensimvis
import pyLDAvis


def save_to_file(*text):

    with open('filename.txt', mode='wt', encoding='utf-8') as myfile:
        for lines in text:
            myfile.write('\n'.join(str(line) for line in lines))
            myfile.write('\n')

tokenizer = RegexpTokenizer(r'\w+')

# create English stop words list
en_stop = get_stop_words('en')

# Create p_stemmer of class PorterStemmer
p_stemmer = PorterStemmer()

doc_set = []
path = 'C:/Mallet/td/*.txt'
files = glob.glob(path)
for name in files:  # 'file' is a builtin type, 'name' is a less-ambiguous variable name.
        with open(name) as f:  # No need to specify 'r': this is the default.
            doc_set.append(f.read())

texts = []

# loop through document list
for i in doc_set:
    # clean and tokenize document string
    raw = i.lower()
    tokens = tokenizer.tokenize(raw)

    # remove stop words from tokens
    stopped_tokens = [i for i in tokens if not i in en_stop]

    # stem tokens
    stemmed_tokens = [p_stemmer.stem(i) for i in stopped_tokens]

    # add tokens to list
    texts.append(stemmed_tokens)

# turn our tokenized documents into a id <-> term dictionary
dictionary = corpora.Dictionary(texts)

# convert tokenized documents into a document-term matrix
corpus = [dictionary.doc2bow(text) for text in texts]

# generate LDA model
ldamodel = gensim.models.LdaModel(corpus, num_topics=2, id2word=dictionary, passes=20)
ldamodel.save('td_model')
f = open('final_topics', 'w')
# print(ldamodel.print_topics(num_topics=2, num_words=4))
# save_to_file(*(ldamodel.print_topics(num_topics=2, num_words=4)))
vis_data = gensimvis.prepare(ldamodel, corpus, dictionary)
pyLDAvis.display(vis_data)
