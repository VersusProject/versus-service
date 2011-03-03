import urllib2
from urllib import urlencode
import sys
import getopt

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

def main(argv=None):
    print "in main"
    if argv is None:
        argv = sys.argv
    try:
        try:
            opts, args = getopt.getopt(argv[1:], "h", ["help"])
        except getopt.error, msg:
            raise Usage(msg)
    except Usage, err:
            print >>sys.stderr, err.msg
            print >>sys.stderr, "for help use --help"
            return 2        

    # process options
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            return 0
    # process arguments
    for arg in args:
        process(arg) # process() is defined elsewhere

    base = 'http://localhost:8183/versus'
    # get adapters
    result = get(base + '/adapters')
    print result
    # submit comparison
    comparison = {
       'endpoint':'http://localhost:8182/versus/comparisons',
       'dataset1':'http://isda.ncsa.illinois.edu/img/ISDA-logo.png',
       'dataset2':'http://isda.ncsa.illinois.edu/img/uiuc-logo-bold50_2.gif',
       'adapter':'edu.illinois.ncsa.versus.adapter.impl.BytesAdapter',
       'extractor':'edu.illinois.ncsa.versus.extract.impl.MD5Extractor',
       'measure':'edu.illinois.ncsa.versus.measure.impl.MD5DistanceMeasure'
    }
    postResult = post(base + '/comparisons', comparison)
    print postResult

if __name__ == "__main__":
    sys.exit(main())

