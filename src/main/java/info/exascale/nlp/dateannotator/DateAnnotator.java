package info.exascale.nlp.dateannotator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import info.exascale.nlp.NLPAnnotators;
import com.sun.istack.internal.Nullable;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;

public class DateAnnotator {

    /**
     * Annotates sentence with TIMEX3 annotations
     * @param sentence - the sentence to be annotated
     * @param date - date of the document, to resolve relative dates, can be {@code null}.
     * @return sentences annotated with TIMEX3 annotations
     */
    public static List<TimeAnnotation> annotate(String sentence, @Nullable String date) {
        Annotation annotation = new Annotation(sentence);

        if (date != null) {
            annotation.set(CoreAnnotations.DocDateAnnotation.class, date);
        }

        NLPAnnotators.dateAnnotationPipeline.annotate(annotation);
        List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);

        List<TimeAnnotation> annotations = new ArrayList<>();
        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            annotations.add(new TimeAnnotation(tokens.get(0).get(CoreAnnotations.TokenBeginAnnotation.class),
                    tokens.get(tokens.size() - 1).get(CoreAnnotations.TokenEndAnnotation.class),
                    cm.get(TimeAnnotations.TimexAnnotation.class),
                    cm.get(TimeExpression.Annotation.class).getTemporal()));
        }
        return annotations;
    }

    /**
     * Annotates sentence with TIMEX3 annotations, for relative dates, date is set to TODAY.
     * @param sentence - the sentence to be annotated
     * @return sentences annotated with TIMEX3 annotations
     */
    public static List<TimeAnnotation> annotate(String sentence) {
        // set date to today if null
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String date = dateFormat.format(new Date());
        return annotate(sentence, date);

    }

    /**
     * Annotates sentence with TIMEX3 annotations, for relative dates, date is set to TODAY.
     * @param sentence - the sentence to be annotated
     * @return sentences annotated with TIMEX3 annotations
     */
    public static String annotateInline(String sentence, String date) {
        List<String> tokens = new ArrayList<>(Arrays.asList(sentence.split(" ")));
        List<TimeAnnotation> annotations = annotate(sentence, date);
        for (TimeAnnotation annotation: annotations) {
            String token = TimeClass.getTimeClass(annotation.temporal);
            for (int i=annotation.startToken+1; i<annotation.endToken; i++)
                tokens.set(i, null);
            tokens.set(annotation.startToken, token);
        }
        tokens.removeAll(Collections.singleton(null));
        return String.join(" ", tokens);
    }

}
