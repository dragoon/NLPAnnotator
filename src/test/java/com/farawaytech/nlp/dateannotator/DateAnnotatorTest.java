package com.farawaytech.nlp.dateannotator;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class DateAnnotatorTest {

    @Test
    public void testAnnotate() throws Exception {
        List<TimeAnnotation> annotationList = DateAnnotator.annotate("Three interesting dates are 18 Feb 1997 , the 20th of july and 4 days from today .", "2015-03-11");
        assert annotationList.get(0).startToken == 4;
        assert annotationList.get(0).endToken == 7;

        assertEquals(annotationList.get(0).timex.toString(), "<TIMEX3 tid=\"t1\" type=\"DATE\" value=\"1997-02-18\">18 Feb 1997</TIMEX3>");
        assertEquals(annotationList.get(1).timex.toString(), "<TIMEX3 tid=\"t2\" type=\"DATE\" value=\"2015-07-20\">the 20th of july</TIMEX3>");
        assertEquals(annotationList.get(2).timex.toString(), "<TIMEX3 tid=\"t3\" type=\"DATE\" value=\"2015-03-15\">4 days from today</TIMEX3>");

        annotationList = DateAnnotator.annotate("It is 11:30pm .", "2015-03-11");
        assertEquals(annotationList.get(0).timex.toString(), "<TIMEX3 tid=\"t1\" type=\"TIME\" value=\"2015-03-11T23:30\">11:30pm</TIMEX3>");
    }
}