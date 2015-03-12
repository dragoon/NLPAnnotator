package com.farawaytech.nlp;

import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.Options;
import edu.stanford.nlp.time.TimeAnnotator;

import java.util.Properties;

public class NLPAnnotators {
    public static POSTaggerAnnotator POS_ANNOTATOR;
    public static TimeAnnotator TIME_ANNOTATOR;
    public static TokenizerAnnotator WHITESPACE_TOKENIZER;
    public static WordsToSentencesAnnotator W2S_ANNOTATOR = new WordsToSentencesAnnotator(false);

    static {
        POS_ANNOTATOR = new POSTaggerAnnotator(false);
        Properties props = new Properties();
        TIME_ANNOTATOR = new TimeAnnotator("sutime", props);
        WHITESPACE_TOKENIZER = new TokenizerAnnotator(false, TokenizerAnnotator.TokenizerType.Whitespace);
    }
}
