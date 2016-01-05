package com.lilalinda.younicos;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Encapsulating a battery power profile, which can be read from an XML file and be validated against a maximum power.
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

    /**
     * Parse given XML file into a power profile.  This will not validate the power profile using semantics
     * (use @link#validate(long) for this) but will fail if XML does not conform to expected format.
     *
     * @param xmlFile file to be parsed into a Java power profile
     * @return newly created power profile
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     */
    public static PowerProfile importFromXML(File xmlFile)
            throws ParserConfigurationException, IOException, SAXException, ParseException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new MyErrorHandler(new PrintWriter(System.out)));

        Document doc = db.parse(xmlFile);

        // use this date format to parse (ignoring the time zone and using local one)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        // go through sequence of tuples:
        SortedSet<PowerDatePair> powerDatePairs = new TreeSet<>();
        NodeList nodeList = doc.getElementsByTagName("younicos:power");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String sPower = element.getElementsByTagName("younicos:power_kW").item(0).getTextContent();
                String sDate = element.getElementsByTagName("younicos:timestamp").item(0).getTextContent();
                powerDatePairs.add(new PowerDatePair(
                        dateFormat.parse(sDate),
                        Long.parseLong(sPower)
                ));
            }
        }

        // now get start and end times, too, and create new power profile:
        return new PowerProfile(
                dateFormat.parse(doc.getElementsByTagName("younicos:start").item(0).getTextContent()),
                dateFormat.parse(doc.getElementsByTagName("younicos:end").item(0).getTextContent()),
                new ArrayList<>(powerDatePairs));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (PowerDatePair pair : powerSequence)
            builder.append(String.format("  %s\n", pair.toString()));
        return String.format("PowerProfile: [%s - %s]\n%s",
                start, end, builder.toString());
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
            // power value not more than max power
            if (Math.abs(pdp.powerInKW) > maxPowerInKW) return false;
        }

        return true;
    }
}
