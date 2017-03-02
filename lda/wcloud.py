from suds.client import Client
url = 'http://agrovoc.fao.org/axis/services/SKOSWS?wsdl'
d = dict(http='10.185.190.10:8080', https='10.185.190.10:8080')
client = Client(url)
client.set_options(proxy=d)
print client


