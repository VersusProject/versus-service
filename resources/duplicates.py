import sys, os, urllib2, urllib
from urllib import urlencode
import itertools
from poster.encode import multipart_encode
from poster.streaminghttp import register_openers

base = 'http://localhost:8182/versus/api'

def get(url):
    try:
        req = urllib2.Request(url)
        httpHandler = urllib2.HTTPHandler()
        httpHandler.set_http_debuglevel(0)
        opener = urllib2.build_opener(httpHandler)
        return opener.open(req).read()
    except urllib2.HTTPError, e:
        print "HTTP error: %d" % e.code
    except urllib2.URLError, e:
        print "Network error: %s" % e.reason.args[1]

def post(url, params):
    encodedparams = urlencode(params)
    try:
        req = urllib2.Request(url, encodedparams)
        httpHandler = urllib2.HTTPHandler()
        httpHandler.set_http_debuglevel(0)
        opener = urllib2.build_opener(httpHandler)
        return opener.open(req).read()
    except urllib2.HTTPError, e:
        print "HTTP error: %d" % e.code
    except urllib2.URLError, e:
        print "Network error: %s" % e.reason.args[1]


def compare(element):
    comparison = {
       'endpoint': base + '/comparisons',
       'dataset1':element[0],
       'dataset2':element[1],
       'adapter':'edu.illinois.ncsa.versus.adapter.impl.BytesAdapter',
       'extractor':'edu.illinois.ncsa.versus.extract.impl.MD5Extractor',
       'measure':'edu.illinois.ncsa.versus.measure.impl.MD5DistanceMeasure'
    }
#    print comparison
    return post(base + '/comparisons', comparison)

def upload(file):
    register_openers()
    datagen, headers = multipart_encode({"fileToUpload": open(file, "rb")})
    request = urllib2.Request(base + "/files/upload", datagen, headers)
    return urllib2.urlopen(request).read()

if __name__ == '__main__':
    
    print "Starting!"
    print "***************************"
    
    fileSet = []
    urlFileMap = {}
    for arg in sys.argv:
        for root, dirs, files in os.walk(arg):
           for file in files:
                print 'File found: ', file
                fileSet.append(root + '/' + file)
                
    print "***************************"
                
    # upload all files
    for file in fileSet:
        id = upload(file)
        fileUrl = base + "/files/" + id
        print "Uploading file ", file, "- File url is ", fileUrl
        urlFileMap[fileUrl] = file

    print "***************************"

    comparisons = {}
    for element in itertools.combinations(urlFileMap.keys(), 2):
        print 'Submitting comparison between ', urlFileMap[element[0]], " and ", urlFileMap[element[1]]
        result = compare(element)
        comparisons[result] = (urlFileMap[element[0]], urlFileMap[element[1]])
        
    print "***************************"
        
    for comparison in comparisons.keys():
        status = urllib2.urlopen(comparison + '/status').read()
        if '0.0' in status:
            print "Files ", comparisons[comparison][0], " and ", comparisons[comparison][1], ' are not duplicates'
        elif '1.0' in status:
            print "Files ", comparisons[comparison][0], " and ", comparisons[comparison][1], ' are *duplicates*'
        else:
            print "Files ", comparisons[comparison][0], " and ", comparisons[comparison][1], ' have not being compared yet. Check ', comparison, ' in a bit'
    
    print "***************************"   
    print "Done!"

