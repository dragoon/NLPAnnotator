package info.exascale.nlp.client;

import com.farawaytech.nlp.dateannotator.thrift.DateAnnotatorService;
import com.farawaytech.nlp.dateannotator.thrift.TAnnotationResponse;
import com.farawaytech.nlp.dateannotator.thrift.TTimeAnnotation;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.protocol.TProtocol;

public class ThriftClient {
    public static void main(String[] args) {


        try {
            TTransport transport = new THttpClient("http://srv.exascale.info:8080/NLPAnnotator/dateannotation");
            transport.open();

            TProtocol protocol = new TCompactProtocol(transport);
            DateAnnotatorService.Client client = new DateAnnotatorService.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(DateAnnotatorService.Client client) throws TException {
        TAnnotationResponse response = client.annotate("Three interesting dates are 18 Feb 1997 , the 20th of july and 4 days from today .", null);
        for (TTimeAnnotation annotation : response.annotations) {
            System.out.println(annotation.startToken);
            System.out.println(annotation.endToken);
            System.out.println(annotation.temporal);
        }
    }
}