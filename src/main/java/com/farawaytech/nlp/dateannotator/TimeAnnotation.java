package com.farawaytech.nlp.dateannotator;


import edu.stanford.nlp.time.Timex;

public class TimeAnnotation {

    public int startToken;
    public int endToken;
    public Timex resolvedTemporal;

    public TimeAnnotation(int startToken, int endToken, Timex timex) {
        this.startToken = startToken;
        this.endToken = endToken;
        this.resolvedTemporal = timex;
    }
}
