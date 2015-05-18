import sys

import DateAnnotatorService
from ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TCompactProtocol

try:

    transport = TSocket.TSocket("localhost", 3030)
    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TFramedTransport(transport)
    protocol = TCompactProtocol.TCompactProtocol(transport)
    client = DateAnnotatorService.Client(protocol)
    # Connect!
    transport.open()

    for line in sys.stdin:
        sentence = line.strip()
        response = client.annotate(sentence.decode('utf-8'), None)
        for time_annotation in response.annotations:
            print time_annotation.startToken
            print time_annotation.endToken
            print time_annotation.temporal

    # Close!
    transport.close()

except Thrift.TException, tx:
    print '%s' % (tx.message)
