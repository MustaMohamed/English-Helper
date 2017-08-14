package com.example.musta.englishhelper.helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by musta on 12/08/17.
 */

public class XMLParser {

    private DocumentBuilderFactory mdbf;
    private InputStream mInStream;
    private Document mDoc;
    private TreeMap<String, WordElement> mAllWordsElements;

    public TreeMap<String, WordElement> getmAllWordsElements() {
        return mAllWordsElements;
    }


    public XMLParser(InputStream in){
        mAllWordsElements = new TreeMap<>();

        //get the factory
        this.mdbf = DocumentBuilderFactory.newInstance();
        this.mInStream = in;
        try {
            DocumentBuilder builder=DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder();
            mDoc = builder.parse(in, null);
            parseDocument();
        }catch(ParserConfigurationException | SAXException | IOException pce) {
            pce.printStackTrace();
        }
    }



    private void parseDocument(){
        //get the root element
        Element docEle = mDoc.getDocumentElement();
        //get a nodelist ofelements
        NodeList nl = docEle.getElementsByTagName("word");
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                //get the employee element
                Element el = (Element)nl.item(i);
                //get the Employee object
                WordElement e = getWordElement(el);
                //add it to list
                mAllWordsElements.put(e.getmSourceEnglishWord(), e);
            }
        }
    }

    private WordElement getWordElement(Element empEl) {

        //for each <employee> element get text or int values of
        //name ,id, age and name
        String name = getTextValue(empEl,"src");
        ArrayList<String> trList;
        trList = getListValue(empEl, "tr");
        //Create a new Employee with the value read from the xml nodes
        WordElement e = new WordElement(name, trList);
        return e;
    }

    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }

    private ArrayList<String> getListValue(Element ele, String tagName) {
        ArrayList<String> textVal = new ArrayList<>();
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                textVal.add(el.getFirstChild().getNodeValue());
            }
        }
        return textVal;
    }

}
