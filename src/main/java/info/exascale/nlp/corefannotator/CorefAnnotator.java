package info.exascale.nlp.corefannotator;


import info.exascale.nlp.NLPAnnotators;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class CorefAnnotator {

    public static void annotate(String document, String date, String docId) {
        // set date to today if null
        if (date == null) {
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
            date = dateFormat.format(new Date());
        }

        Annotation annotation = new Annotation(document);
        annotation.set(CoreAnnotations.DocDateAnnotation.class, date);
        NLPAnnotators.corefPipeline.annotate(annotation);
        Map<Integer, CorefChain> corefChains = annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Integer, CorefChain> entry: corefChains.entrySet()) {
            CorefChain chain = entry.getValue();

            List<CorefChain.CorefMention> mentions = chain.getMentionsInTextualOrder();
            for (CorefChain.CorefMention mention: mentions) {
                StringJoiner line = new StringJoiner("\t");
                lines.add(docId);
                line.add(String.valueOf(mention.startIndex));
                line.add(String.valueOf(mention.endIndex));
                line.add(mention.mentionSpan);
                line.add(String.valueOf(entry.getKey()));
                lines.add(line.toString());
            }
        }

        try {
            Files.write(Paths.get("corefs.txt"), lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        annotate("I walked into John's house. It was empty. John came late. He was tired.", "2015-03-15", "1");
    }
}
