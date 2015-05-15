package info.exascale.nlp.server;

import com.farawaytech.nlp.dateannotator.thrift.DateAnnotatorService;
import com.farawaytech.nlp.dateannotator.thrift.impl.DateAnnotatorServiceImpl;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.*;

/*
This is a very simple Java Server that implements the thrift protocol. This
class does mainly nothing but forwards the action to the handler of the
service.
*/
public class Server {

    public static DateAnnotatorServiceImpl handler;
    public static DateAnnotatorService.Processor<DateAnnotatorServiceImpl> processor;

    public static void main(String[] args) throws TTransportException {

        if (args.length < 1) {
            System.out.println("Usage: java server [port]");
            return;
        }

        handler = new DateAnnotatorServiceImpl();
        processor = new DateAnnotatorService.Processor<>(handler);

        TNonblockingServerTransport trans = new TNonblockingServerSocket(Integer.parseInt(args[0]));
        TThreadedSelectorServer.Args args1 = new TThreadedSelectorServer.Args(trans);
        args1.transportFactory(new TFramedTransport.Factory());
        args1.protocolFactory(new TCompactProtocol.Factory());
        args1.processor(processor);
        args1.selectorThreads(4);
        args1.workerThreads(32);
        TServer server = new TThreadedSelectorServer(args1);
        server.serve();
    }
}