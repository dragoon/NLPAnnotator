package info.exascale.nlp.dateannotator;

import edu.stanford.nlp.time.SUTime;

import java.time.Duration;

public class TimeClass {
    public static String DATE = "<TIMEX:DATE>";
    public static String TIME = "<TIMEX:TIME>";
    public static String MONTH = "<TIMEX:MONTH>";
    public static String INTERVAL = "<TIMEX:INTERVAL>";


    public static String getTimeClass(SUTime.Temporal temporal) {
        SUTime.Duration type = temporal.getDuration();
        return "<TIMEX:"+type.toString()+">";
    }
}
