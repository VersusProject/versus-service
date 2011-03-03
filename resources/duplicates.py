import sys, os
import itertools

fileSet = []
for arg in sys.argv:
    for root, dirs, files in os.walk(arg):
        for file in files:
            print file
            fileSet.append(file)

for element in itertools.combinations(fileSet, 2):
    print "Comparing ", element
