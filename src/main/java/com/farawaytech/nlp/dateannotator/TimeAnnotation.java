package com.farawaytech.nlp.dateannotator;


import edu.stanford.nlp.time.SUTime;

public class TimeAnnotation {

    public int startToken;
    public int endToken;
    public SUTime.Temporal resolvedTemporal;

    public TimeAnnotation(int startToken, int endToken, SUTime.Temporal resolvedTemporal) {
        this.startToken = startToken;
        this.endToken = endToken;
        this.resolvedTemporal = resolvedTemporal;
    }
}
