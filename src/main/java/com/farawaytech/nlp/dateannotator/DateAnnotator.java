package com.farawaytech.nlp.dateannotator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.farawaytech.nlp.NLPAnnotators;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;

public class DateAnnotator {

    public static List<TimeAnnotation> annotate(String sentence, String date) {
        // set date to today if null
        if (date == null) {
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            date = dateFormat.format(new Date());
        }

        AnnotationPipeline pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(NLPAnnotators.WHITESPACE_TOKENIZER);
        pipeline.addAnnotator(NLPAnnotators.W2S_ANNOTATOR);
        pipeline.addAnnotator(NLPAnnotators.POS_ANNOTATOR);
        pipeline.addAnnotator(NLPAnnotators.TIME_ANNOTATOR);

        Annotation annotation = new Annotation(sentence);
        annotation.set(CoreAnnotations.DocDateAnnotation.class, date);
        pipeline.annotate(annotation);
        List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);

        List<TimeAnnotation> annotations = new ArrayList<>();
        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            annotations.add(new TimeAnnotation(tokens.get(0).get(CoreAnnotations.TokenBeginAnnotation.class),
                    tokens.get(tokens.size() - 1).get(CoreAnnotations.TokenEndAnnotation.class),
                    cm.get(TimeAnnotations.TimexAnnotation.class)));
        }
        return annotations;
    }

}
