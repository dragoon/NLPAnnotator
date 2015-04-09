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

    // PIPELINES
    public static AnnotationPipeline dateAnnotationPipeline;
    public static AnnotationPipeline corefPipeline;

    static {
        Properties props = new Properties();
        TIME_ANNOTATOR = new TimeAnnotator("sutime", props);
        WHITESPACE_TOKENIZER = new TokenizerAnnotator(false, TokenizerAnnotator.TokenizerType.Whitespace);
        props = new Properties();
        props.setProperty(Constants.DEMONYM_PROP, DefaultPaths.DEFAULT_DCOREF_DEMONYM);
        props.setProperty(Constants.ANIMATE_PROP, DefaultPaths.DEFAULT_DCOREF_ANIMATE);
        props.setProperty(Constants.INANIMATE_PROP, DefaultPaths.DEFAULT_DCOREF_INANIMATE);
        corefPipeline = new StanfordCoreNLP(props);

        dateAnnotationPipeline = new AnnotationPipeline();
        dateAnnotationPipeline.addAnnotator(WHITESPACE_TOKENIZER);
        dateAnnotationPipeline.addAnnotator(W2S_ANNOTATOR);
        dateAnnotationPipeline.addAnnotator(POS_ANNOTATOR);
        dateAnnotationPipeline.addAnnotator(TIME_ANNOTATOR);

    }
}
