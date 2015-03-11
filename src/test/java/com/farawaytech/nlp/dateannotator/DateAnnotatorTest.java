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

        assertEquals(annotationList.get(0).resolvedTemporal.toString(), "1997-02-18");
        assertEquals(annotationList.get(1).resolvedTemporal.toString(), "2015-07-20");
        assertEquals(annotationList.get(2).resolvedTemporal.toString(), "2015-03-15");
    }
}