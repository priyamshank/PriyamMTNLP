import locale
import glob
import os.path
import requests
import tarfile

dirname = 'data'
folders = ['data/AdTweets', 'data/AwarenessTweets', 'data/NewsTweets', 'data/OtherTweets', 'data/ResearchTweets','data/SelfTweets']
alldata = u''


def normalize_text(text):
    norm_text = text.lower()

    # Replace breaks with spaces
    norm_text = norm_text.replace('<br />', ' ')

    # Pad punctuation with spaces on both sides
    for char in ['.', '"', ',', '(', ')', '!', '?', ';', ':']:
        norm_text = norm_text.replace(char, ' ' + char + ' ')

    return norm_text


for fol in folders:
    temp = u''
    output = fol.replace('/', '-') + '.txt'

    # Is there a better pattern to use?
    txt_files = glob.glob('/'.join([dirname, fol, '*.txt']))

    for txt in txt_files:
        with open(txt, 'r', encoding='utf-8') as t:
            control_chars = [chr(0x85)]
            t_clean = t.read()

            for c in control_chars:
                t_clean = t_clean.replace(c, ' ')
                # print t_clean
            temp += t_clean
            # print temp
        temp += "\n"

    temp_norm = normalize_text(temp)
    with open('/'.join([dirname, output]), 'w') as n:
        n.write(temp_norm)

    alldata += temp_norm

with open('/'.join([dirname, 'alldata-id.txt']), 'w') as f:
    for idx, line in enumerate(alldata.splitlines()):
        num_line = "_*{0} {1}\n".format(idx, line)
        f.write(num_line)