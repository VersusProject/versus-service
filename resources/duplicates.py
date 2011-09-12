import sys, os, urllib2, urllib
from urllib import urlencode
import itertools
from poster.encode import multipart_encode
from poster.streaminghttp import register_openers
import time

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

    print "***************************"
    print "Starting!"
    print "***************************"
    
    #base = 'http://localhost:8182/versus/api'
    base = sys.argv[1]
    
    fileSet = {}
    urlFileMap = {}
    for arg in sys.argv[2:]:
        for root, dirs, files in os.walk(arg):
           for file in files:
                print 'File found: ', file
                fileSet[root + '/' + file] = (root, file)
                
    print "***************************"
                
    # upload all files
    print 'Uploading files...'
    for file in fileSet.keys():
        id = upload(file)
        fileUrl = base + "/files/" + id
        #print "Uploading file ", file, "- File url is ", fileUrl
        urlFileMap[fileUrl] = file
    print len(fileSet.keys()), ' files uploaded'
    
    print "***************************"

    comparisons = {}
    print 'Submitting comparisons...'
    for element in itertools.combinations(urlFileMap.keys(), 2):
        #print 'Submitting comparison between ', urlFileMap[element[0]], " and ", urlFileMap[element[1]]
        result = compare(element)
        comparisons[result] = (urlFileMap[element[0]], urlFileMap[element[1]])
    print len(comparisons.keys()), " comparisons submitted"    
        
    time.sleep(10)    
    
    print "***************************"
    
    duplicates = []
    nonduplicates = []
    for comparison in comparisons.keys():
        status = urllib2.urlopen(comparison + '/value').read()
        if '0.0' in status:
            #print "Files ", comparisons[comparison][0], " and ", comparisons[comparison][1], ' are not duplicates'
            nonduplicates.append(comparison)
        elif '1.0' in status:
            #print "Files ", comparisons[comparison][0], " and ", comparisons[comparison][1], ' are *duplicates*'
            duplicates.append(comparison)
        else:
            print "Files ", comparisons[comparison][0], " and ", comparisons[comparison][1], ' have not being compared yet. Check ', comparison, ' in a bit'
    
    print "Duplicates found: ", len(duplicates)
    for comparison in duplicates:
        print fileSet[comparisons[comparison][0]][1], " and ", fileSet[comparisons[comparison][1]][1]
    
    print "***************************"  
    print "Done!"
    print "***************************"

