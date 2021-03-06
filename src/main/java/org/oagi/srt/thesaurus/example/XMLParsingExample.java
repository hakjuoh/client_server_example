package org.oagi.srt.thesaurus.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class XMLParsingExample {

    public Document parse(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("students.xsd"));
        documentBuilderFactory.setSchema(schema);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(xmlFile);
    }

    public static void main(String[] args) throws Exception {
        XMLParsingExample parser = new XMLParsingExample();
        Document document = parser.parse(new File("students.xml"));

        Element rootElement = document.getDocumentElement();
        System.out.println("Root element name: " + rootElement.getNodeName());

        NodeList nodeList = rootElement.getChildNodes();
        for (int i = 0, len = nodeList.getLength(); i < len; ++i) {
            Node child = nodeList.item(i);

            System.out.println("Child element name: " + child.getNodeName());
        }
    }
}
