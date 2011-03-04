import sys, os
import itertools

def compare(element):
    pass

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

