package info.exascale.nlp.servlet;

import com.farawaytech.nlp.dateannotator.thrift.DateAnnotatorService;
import com.farawaytech.nlp.dateannotator.thrift.impl.DateAnnotatorServiceImpl;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServlet;

import javax.servlet.annotation.WebServlet;


@WebServlet(name = "DateAnnotationServlet", urlPatterns = "/dateannotation", loadOnStartup = 1,
        description = "Thrift Servlet to annotate text with temporal information")
public class DateAnnotationServlet extends TServlet {
    public DateAnnotationServlet()
    {
        super(new DateAnnotatorService.Processor<>(new DateAnnotatorServiceImpl()), new TCompactProtocol.Factory()
        );
    }

}
