package com.farawaytech.nlp;

import edu.stanford.nlp.dcoref.Constants;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.TimeAnnotator;

import java.io.IOException;
import java.util.Properties;

public class NLPAnnotators {
    public static POSTaggerAnnotator POS_ANNOTATOR = new POSTaggerAnnotator(false);
    public static TimeAnnotator TIME_ANNOTATOR;
    public static TokenizerAnnotator WHITESPACE_TOKENIZER;
    public static WordsToSentencesAnnotator W2S_ANNOTATOR = new WordsToSentencesAnnotator(false);
    public static ParserAnnotator PARSER_ANNOTATOR = new ParserAnnotator(false, -1);
    public static NERCombinerAnnotator NER_ANNOTATOR;
    public static DeterministicCorefAnnotator COREF_ANNOTATOR;

    // PIPELINES
    public static AnnotationPipeline dateAnnotationPipeline;

    static {
        Properties props = new Properties();
        TIME_ANNOTATOR = new TimeAnnotator("sutime", props);
        WHITESPACE_TOKENIZER = new TokenizerAnnotator(false, TokenizerAnnotator.TokenizerType.Whitespace);
        try {
            NER_ANNOTATOR = new NERCombinerAnnotator(false);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        props = new Properties();
        props.setProperty(Constants.DEMONYM_PROP, DefaultPaths.DEFAULT_DCOREF_DEMONYM);
        props.setProperty(Constants.ANIMATE_PROP, DefaultPaths.DEFAULT_DCOREF_ANIMATE);
        props.setProperty(Constants.INANIMATE_PROP, DefaultPaths.DEFAULT_DCOREF_INANIMATE);
        COREF_ANNOTATOR = new DeterministicCorefAnnotator(props);

        dateAnnotationPipeline = new AnnotationPipeline();
        dateAnnotationPipeline.addAnnotator(NLPAnnotators.WHITESPACE_TOKENIZER);
        dateAnnotationPipeline.addAnnotator(NLPAnnotators.W2S_ANNOTATOR);
        dateAnnotationPipeline.addAnnotator(NLPAnnotators.POS_ANNOTATOR);
        dateAnnotationPipeline.addAnnotator(NLPAnnotators.TIME_ANNOTATOR);
    }
}
