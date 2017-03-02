import requests
import argparse

parser = argparse.ArgumentParser(description='Fetch wikipedia extracts.')
parser.add_argument('word', help='word to define')
args = parser.parse_args()

proxies = {
    # See http://www.mediawiki.org/wiki/API:Main_page#API_etiquette
     "http": "10.185.190.10:8080",
     "https": "10.185.190.10:8080"
}

headers = {
    # http://www.mediawiki.org/wiki/API:Main_page#Identifying_your_client
    "User-Agent": "Definitions/1.0 (Contact rob@example.com for info.)"
}

params = {
    'action':'query',
    'prop':'categories',
    'format':'json',
    'exintro':1,
    'explaintext':1,
    'generator':'search',
    'gsrsearch':args.word,
    'gsrlimit':1,
    'continue':''
}

r = requests.get('http://en.wikipedia.org/w/api.php',
                 params=params,
                 headers=headers,
                 proxies=proxies)
json = r.json()
if "query" in json:
    result = json["query"]["pages"].items()[0][1]["categories"]
    print result
else:
    print "No definition."