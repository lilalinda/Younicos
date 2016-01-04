package com.lilalinda.younicos;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Encapsulating a battery power profile read and validated from an XML file.
 */
public final class PowerProfile {

    final Date start, end;
    final List<PowerDatePair> powerSequence;

    protected PowerProfile(Date start, Date end, List<PowerDatePair> powerSequence) {
        if (start == null || end == null)
            throw new IllegalArgumentException("Cannot create power profile with NULL for start or end date");
        this.start = start;
        this.end = end;
        if (powerSequence == null || powerSequence.isEmpty())
            throw new IllegalArgumentException("Cannot create power profile with NULL or empty sequence");
        this.powerSequence = powerSequence;
    }

    public static PowerProfile importFromXML(File xmlFile)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new MyErrorHandler(new PrintWriter(System.out)));

        Document doc = db.parse(xmlFile);

        // TODO: obtain DOM elements for power profile
        Date d = new Date();
        List<PowerDatePair> l = new ArrayList<PowerDatePair>();
        l.add(new PowerDatePair(new Date(d.getTime() + 1000L), 1000));

        return new PowerProfile(d, new Date(d.getTime() + 3000L), l);
    }

    /**
     * Validate semantics of this power profile against a given maximum value of power (changes?).
     * A valid power profile has a non-empty time interval between start and end times, and all
     * time stamps of the power sequence are within this interval.  Furthermore, the sequence has
     * ordered times stamps.  And finally, all the power values are within a given power range.
     *
     * @param maxPowerInKW maximum power (change?) in kW
     * @return true, if valid and false otherwise
     */
    public boolean validate(long maxPowerInKW) {
        // TODO check semantics
        // end after start time
        if (end.before(start)) return false;
        if (start.getTime() == end.getTime()) return false;

        Date lastTimeStamp = start;
        for (PowerDatePair pdp : powerSequence) {
            // sequence times in [start, end] interval
            if (pdp.timestamp.before(start)) return false;
            if (pdp.timestamp.after(end)) return false;
            // sequence times ordered
            if (pdp.timestamp.before(lastTimeStamp)) return false;
            lastTimeStamp = pdp.timestamp;
            // power value changes not more than max power (or absolute values?)
            if (Math.abs(pdp.powerInKW) > maxPowerInKW) return false;
        }

        return true;
    }
}
