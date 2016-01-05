package com.lilalinda.younicos;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for power profiles.
 */
public final class TestPowerProfile {

    // for sample power profiles
    private Date start = new Date();
    private List<PowerDatePair> list = new ArrayList<PowerDatePair>();
    {
        list.add(new PowerDatePair(new Date(start.getTime() + 500L), -1000L));
    }

    // --- exceptions

    @Test(expected = RuntimeException.class)
    public void readFromNULL() throws IOException, SAXException, ParserConfigurationException, ParseException {
        PowerProfile pp = PowerProfile.importFromXML(null);
    }

    // --- success

    @Test
    public void withValidPP() throws IOException {
        PowerProfile pp = new PowerProfile(
                start,
                new Date(start.getTime() + 4000L),
                list);
        assertTrue("valid power profile", pp.validate(6000L));
    }

    // --- semantic problems

    @Test
    public void endBeforeStart() {
        PowerProfile pp = new PowerProfile(
                start,
                new Date(start.getTime() - 2000L),
                list);
        assertFalse("end before start", pp.validate(6000L));
    }

    @Test
    public void endEqualsStart() {
        PowerProfile pp = new PowerProfile(start, start, list);
        assertFalse("end equals start", pp.validate(6000L));
    }

    @Test
    public void powerTooHigh() {
        PowerProfile pp = new PowerProfile(
                start,
                new Date(start.getTime() + 4000L),
                list);
        assertFalse("power too high", pp.validate(600L));
    }

    @Test
    public void sequenceNotOrdered() {
        List<PowerDatePair> updatedList = new ArrayList<>(list);
        updatedList.add(new PowerDatePair(new Date(start.getTime() + 100L), 5000L));
        PowerProfile pp = new PowerProfile(
                start,
                new Date(start.getTime() + 4000L),
                updatedList);
        assertFalse("unordered list", pp.validate(6000L));
    }

    @Test
    public void sequenceBeforeStart() {
        List<PowerDatePair> updatedList = new ArrayList<>(list);
        updatedList.add(new PowerDatePair(new Date(start.getTime() - 100L), 500L));
        PowerProfile pp = new PowerProfile(
                start,
                new Date(start.getTime() + 4000L),
                updatedList);
        assertFalse("sequence before START", pp.validate(6000L));
    }

    @Test
    public void sequenceAfterEnd() {
        List<PowerDatePair> updatedList = new ArrayList<>(list);
        updatedList.add(new PowerDatePair(new Date(start.getTime() + 5000L), -3000L));
        PowerProfile pp = new PowerProfile(
                start,
                new Date(start.getTime() + 4000L),
                updatedList);
        assertFalse("sequence after END", pp.validate(6000L));
    }
}
