package project2;//package project2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by DotinSchool2 on 10/21/2015.
 */
public class XmlParser {
    String fileName;

    public XmlParser(String name) {
        fileName = name;

    }

    public ArrayList<Transaction> readXmlFile() {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

        try {

            File fXmlFile = new File("input.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();


            String terminalId = ((Element) doc.getElementsByTagName("terminal").item(0)).getAttribute("id");
            String terminalType = ((Element) doc.getElementsByTagName("terminal").item(0)).getAttribute("type");
            System.out.println("terminal Id:   " + terminalId + "...");
            System.out.println("terminal Type:   " + terminalType + "...");
            Node server = doc.getElementsByTagName("server").item(0);
            String serverIp = ((Element) doc.getElementsByTagName("server").item(0)).getAttribute("ip");
            String serverPort = ((Element) doc.getElementsByTagName("server").item(0)).getAttribute("port");
            System.out.println("server Ip:   " + serverIp + "...");
            System.out.println("port:   " + serverPort + "...");
            String outLog = ((Element) doc.getElementsByTagName("outLog").item(0)).getAttribute("path");
            System.out.println("outLog path:   " + outLog + "...");
            NodeList transactions = doc.getElementsByTagName("transaction");
            for (int i = 0; i < transactions.getLength(); i++) {
                Node n = transactions.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) n;
                    String transactionId = (element.getAttribute("id"));
                    String transactionType = element.getAttribute("type");
                    Integer transactionamount = Integer.parseInt(element.getAttribute("amount"));
                    String transactiondeposit = element.getAttribute("deposit");
                    System.out.println("transactionId:   " + transactionId + "...");
                    Transaction transaction = new Transaction(terminalId, terminalType, serverIp, serverPort, outLog, transactionId, transactionType, transactionamount, transactiondeposit);
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


    public static void main(String[] args) throws IOException {


    }


}