package project2;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingClient extends Thread {
    String serverName = "localhost";

    String inputFileName;
    Logger logger;


    public GreetingClient(String inputFileName) {

        this.inputFileName = inputFileName;

    }

    @Override
    public void run() {
        try {

            XmlParser terminalFile = new XmlParser(inputFileName);
            Handler fileHandler = new FileHandler(terminalFile.extractOutlogPassFromFile(), true);
            logger = Logger.getLogger(GreetingClient.class.getName());
            logger.setUseParentHandlers(false);
            logger.addHandler(fileHandler);
            int port = terminalFile.extractPortNumberFromFile();
            System.out.println(Thread.currentThread().getName() + ": " + "Client  has been started");
            logger.log(Level.INFO, Thread.currentThread().getName() + "Connecting to " + serverName + " on port " + port + "\n");
            Socket client = new Socket(serverName, port);
            String terminal = terminalFile.extractTerminalIDFromFile();
            sendTerminalTransactions(client);
            System.out.println(Thread.currentThread().getName() + terminal + ": has sent all of its transactions");
            // client.close();
            logger.log(Level.INFO, terminal + "end of connection to " + client.getRemoteSocketAddress() + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void sendTerminalTransactions(Socket client) throws IOException, ParserConfigurationException, TransformerException {

        // ????????????????????????
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//            Document doc = docBuilder.newDocument();
//            Element rootElement = doc.createElement("Transactions");
//            doc.appendChild(rootElement);
        XmlParser FileReader = new XmlParser(inputFileName);
        FileWriter response = new FileWriter(inputFileName+"response.xml", true);
        ArrayList<Transaction> transactions = FileReader.readXmlFile();
        DataInputStream inputResult = new DataInputStream(client.getInputStream());
        logger.log(Level.INFO, Thread.currentThread().getName() + "Just connected to " + client.getRemoteSocketAddress() + "\n");
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        response.write("<Transactions>\n");
        for (Transaction transaction : transactions) {

            logger.log(Level.INFO, Thread.currentThread().getName() + "sending transaction--- transactionid:" + transaction.transactionId + "\n");
            System.out.println(Thread.currentThread().getName() + "sending transaction" + transaction.terminalId + "  " + (transaction.transactionId));
            out.writeObject(transaction);
            String transactionResult;
            int transactionStatus = inputResult.read();
            if (transactionStatus == 1) {
                transactionResult = "successful";
            } else {
                transactionResult = "Fail";
            }
            System.out.println("result of transaction:" + transaction.transactionId + " is: " + transactionResult);
            //??????????????????
//                Element transactionInfo = doc.createElement("Transaction");
//                rootElement.appendChild(transactionInfo);
//                transactionInfo.setAttribute("Id",transaction.transactionId);
//                transactionInfo.setAttribute("result:",transactionResult);

            response.write("<Transaction id= \"" + transaction.transactionId + "\" result=\"" + transactionResult + "\"/>\n");
        }
        response.write("</Transactions>\n");
        response.close();
        out.writeObject(null);
        //????????????????
//               // write the content into xml file
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(new File("file.xml"));
//        // Output to console for testing
//        // StreamResult result = new StreamResult(System.out);
//        transformer.transform(source, result);
//        System.out.println("File saved!");

    }

}