import sys

import DateAnnotatorService
from ttypes import *

from thrift import Thrift
from thrift.transport import THttpClient
from thrift.protocol import TCompactProtocol

try:

    transport = THttpClient.THttpClient("http://srv.exascale.info:8080/NLPAnnotator/dateannotation")
    protocol = TCompactProtocol.TCompactProtocol(transport)
    client = DateAnnotatorService.Client(protocol)
    # Connect!
    transport.open()

    for line in sys.stdin:
        sentence = line.strip()
        response = client.annotate(sentence, None)
        for time_annotation in response.annotations:
            print time_annotation.startToken
            print time_annotation.endToken
            print time_annotation.temporal

    # Close!
    transport.close()

except Thrift.TException, tx:
    print '%s' % (tx.message)
