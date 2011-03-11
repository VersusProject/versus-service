import sys, os, urllib2
from urllib import urlencode
import itertools

def get(url):
    try:
        req = urllib2.Request(url)
        httpHandler = urllib2.HTTPHandler()
        httpHandler.set_http_debuglevel(1)
        opener = urllib2.build_opener(httpHandler)
        return opener.open(req)
    except urllib2.HTTPError, e:
        print "HTTP error: %d" % e.code
    except urllib2.URLError, e:
        print "Network error: %s" % e.reason.args[1]

def post(url, params):
    encodedparams = urlencode(params)
    try:
        req = urllib2.Request(url, encodedparams)
        httpHandler = urllib2.HTTPHandler()
        httpHandler.set_http_debuglevel(1)
        opener = urllib2.build_opener(httpHandler)
        return opener.open(req)
    except urllib2.HTTPError, e:
        print "HTTP error: %d" % e.code
    except urllib2.URLError, e:
        print "Network error: %s" % e.reason.args[1]


def compare(element):
    comparison = {
       'endpoint':'http://localhost:8182/versus/comparisons',
       'dataset1':element[0],
       'dataset2':element[1],
       'adapter':'edu.illinois.ncsa.versus.adapter.impl.BytesAdapter',
       'extractor':'edu.illinois.ncsa.versus.extract.impl.MD5Extractor',
       'measure':'edu.illinois.ncsa.versus.measure.impl.MD5DistanceMeasure'
    }
    print comparison
    #postResult = post(base + '/comparisons', comparison)

if __name__ == '__main__':
    fileSet = []
    for arg in sys.argv:
        for root, dirs, files in os.walk(arg):
           for file in files:
                print 'File found: ', file
                fileSet.append(file)

    for element in itertools.combinations(fileSet, 2):
        print 'Comparing files ', element
        compare(element)

