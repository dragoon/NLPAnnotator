package com.farawaytech.nlp.dateannotator;


import edu.stanford.nlp.time.SUTime;
import edu.stanford.nlp.time.Timex;

public class TimeAnnotation {

    public int startToken;
    public int endToken;
    public SUTime.Temporal temporal;
    private Timex timex;

    public TimeAnnotation(int startToken, int endToken, Timex timex, SUTime.Temporal temporal) {
        this.startToken = startToken;
        this.endToken = endToken;
        this.temporal = temporal;
        this.timex = timex;
    }

    public String getTimexString() {
        return timex.toString();
    }
}
