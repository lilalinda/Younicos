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
    final List<String> powerSequence;

    public PowerProfile(Date start, Date end, List<String> powerSequence) {
        if (start == null || end == null)
            throw new IllegalArgumentException("Cannot create power profile with NULL for start or end date");
        this.start = start;
        this.end = end;
        if (powerSequence == null || powerSequence.isEmpty())
            throw new IllegalArgumentException("Cannot create power profile with NULL or empty sequence");
        this.powerSequence = powerSequence;
    }

    static PowerProfile importFromXML(File xmlFile)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new MyErrorHandler(new PrintWriter(System.out)));

        Document doc = db.parse(xmlFile);

        List<String> l = new ArrayList<String>();
        l.add("eins");
        return new PowerProfile(new Date(), new Date(), l);
    }

    public boolean validate(float maxPower) {
        // TODO check semantics
        // end after start time
        // sequences start time
        // sequences times ordered
        // power value changes not more than max power
        return true;
    }
}
