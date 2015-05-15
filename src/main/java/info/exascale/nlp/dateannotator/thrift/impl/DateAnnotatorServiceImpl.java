package info.exascale.nlp.dateannotator.thrift.impl;

import java.util.ArrayList;
import java.util.List;

import info.exascale.nlp.dateannotator.DateAnnotator;
import info.exascale.nlp.dateannotator.TimeAnnotation;
import info.exascale.nlp.dateannotator.thrift.DateAnnotatorService;
import info.exascale.nlp.dateannotator.thrift.TAnnotationResponse;
import info.exascale.nlp.dateannotator.thrift.TTimeAnnotation;
import org.apache.thrift.TException;


/*
This is the service provider implementation. Please make sure that your
actions are idempotent and the goal should be that the lifetime of the service
handler is longer than a single request.
 */
public class DateAnnotatorServiceImpl implements DateAnnotatorService.Iface {


    public DateAnnotatorServiceImpl() {

    }//Constructor


    @Override
    public TAnnotationResponse annotate(String sentence, String date)
            throws TException {
        ArrayList<TTimeAnnotation> response = new ArrayList<>();
        List<TimeAnnotation> result = DateAnnotator.annotate(sentence, date);
        for (TimeAnnotation annotation: result) {
            response.add(new TTimeAnnotation(String.valueOf(annotation.startToken),
                    String.valueOf(annotation.endToken),
                    annotation.getTimexString()));
        }
        return new TAnnotationResponse(response);
    }//annotate

    @Override
    public String annotateInline(String sentence, String date) throws TException {
        return DateAnnotator.annotateInline(sentence, date);
    }


}
