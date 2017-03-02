import nltk

essays = u"""right about now I'm wishing there was a magic spray to kill fusarium"""
tokens = nltk.word_tokenize(essays)
tagged = nltk.pos_tag(tokens)
nouns = [word for word, pos in tagged
         if (pos == 'NN' or pos == 'NNP' or pos == 'NNS' or pos == 'NNPS')]
downcased = [x.lower() for x in nouns]
joined = " ".join(downcased).encode('utf-8')
into_string = str(nouns)

output = open("output.txt", "w")
output.write(joined)
output.close()
