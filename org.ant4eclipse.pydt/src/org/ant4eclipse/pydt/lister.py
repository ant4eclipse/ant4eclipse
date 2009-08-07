import sys

if __name__ == "__main__":
    print "ANT4ECLIPSE-BEGIN"
    print "_".join([".".join([repr(x) for x in sys.version_info[:-2]]),sys.version_info[3]])
    for i in sys.path:
        print "[" + i + "]"
    print "ANT4ECLIPSE-END"

