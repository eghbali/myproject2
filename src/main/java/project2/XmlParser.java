package project2;//package project2;

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
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by DotinSchool2 on 10/21/2015.
 */
public class XmlParser {
    String fileName;
    Integer serverPort;
    String outLog;
    String terminalId;

    public XmlParser(String name) {
        fileName = name;

    }

    public ArrayList<Transaction> readXmlFile() {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

        try {

            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            terminalId = ((Element) doc.getElementsByTagName("terminal").item(0)).getAttribute("id");
            String terminalType = ((Element) doc.getElementsByTagName("terminal").item(0)).getAttribute("type");
            Node server = doc.getElementsByTagName("server").item(0);
            String serverIp = ((Element) doc.getElementsByTagName("server").item(0)).getAttribute("ip");
            serverPort = Integer.parseInt(((Element) doc.getElementsByTagName("server").item(0)).getAttribute("port"));
            outLog = ((Element) doc.getElementsByTagName("outLog").item(0)).getAttribute("path");
            NodeList transactions = doc.getElementsByTagName("transaction");
            for (int i = 0; i < transactions.getLength(); i++) {
                Node n = transactions.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) n;
                    String transactionId = (element.getAttribute("id"));
                    String transactionType = element.getAttribute("type");
                    Integer transactionAmount = Integer.parseInt(element.getAttribute("amount"));
                    String transactionDeposit = element.getAttribute("deposit");
                    Transaction transaction = new Transaction(terminalId, terminalType, outLog, serverIp, serverPort, transactionId,
                            transactionType, new BigDecimal(transactionAmount), transactionDeposit);
                    transactionList.add(transaction);
//                     getElementsByTagName("transaction").item(0).getTextContent();
//                    System.out.println(i +"  terminal id: "+ x);
//                     int type=(int) element.getElementsByTagName("transaction").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return (transactionList);
    }

    public int extractPortNumberFromFile() {
        try {
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            serverPort = Integer.parseInt(((Element) doc.getElementsByTagName("server").item(0)).getAttribute("port"));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    public String extractOutlogPassFromFile() {

        try {
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            outLog = ((Element) doc.getElementsByTagName("outLog").item(0)).getAttribute("path");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outLog;
    }

    public String extractTerminalIDFromFile() {
        try {
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            terminalId = ((Element) doc.getElementsByTagName("terminal").item(0)).getAttribute("id");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return terminalId;

    }


}