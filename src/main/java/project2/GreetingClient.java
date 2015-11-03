package project2;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreetingClient extends Thread {
    String serverName = "localhost";
    int port;
    String inputFileName;
    Logger logger;

    // Logger logger = Logger.getLogger(GreetingClient.class.getName());
    public GreetingClient(int port, String inputFileName) {
        this.port = port;
        this.inputFileName = inputFileName;
        //  logger.setLevel(Level.SEVERE);
    }

    @Override
    public void run() {
        try {


            Handler fileHandler = new FileHandler(extractOutlogPassFromFile(), true);
            logger = Logger.getLogger(GreetingServer.class.getName());
            logger.addHandler(fileHandler);
            int port = extractPortNumberFromFile();
            System.out.println(Thread.currentThread().getName() + ": " + "Client  has been started");
            logger.log(Level.INFO, Thread.currentThread().getName() +"Connecting to " + serverName + " on port " + port +  "\n");
            Socket client = new Socket(serverName, port);
            String terminal=extractTerminalIDFromFile();
            //khandane file terminal va sakhtane liste transaction ha
            sendTerminalTransactions(client);
            System.out.println(Thread.currentThread().getName() + terminal +": has sent all of its transactions");
          // client.close();
            logger.log(Level.INFO,  terminal+"end of connection to " + client.getRemoteSocketAddress() + "\n");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String extractOutlogPassFromFile() {
        XmlParser fileReader = new XmlParser(inputFileName);
        fileReader.readXmlFile();
        return fileReader.outLog;
    }

    public int extractPortNumberFromFile() {
        XmlParser fileReader = new XmlParser(inputFileName);
        fileReader.readXmlFile();
        return fileReader.serverPort;
    }
    public String extractTerminalIDFromFile() {
        XmlParser fileReader = new XmlParser(inputFileName);
        fileReader.readXmlFile();
        return fileReader.terminalId;
    }

    private void sendTerminalTransactions(Socket client) throws IOException {
        XmlParser FileReader = new XmlParser(inputFileName);
        ArrayList<Transaction> transactions = FileReader.readXmlFile();
        logger.log(Level.INFO, Thread.currentThread().getName()+"Just connected to " + client.getRemoteSocketAddress() + "\n");
        DataInputStream input = new DataInputStream(client.getInputStream());
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        output.writeInt(transactions.size());
        output.writeUTF(transactions.get(0).terminalId);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        for (Transaction transaction : transactions) {
            logger.log(Level.INFO,Thread.currentThread().getName()+ "sending transaction--- transactionid:" + transaction.transactionId + "\n");
            System.out.println(Thread.currentThread().getName()+"sending transaction"+transaction.terminalId+"  "+(transaction.transactionId));
            out.writeObject(transaction);
            //System.out.println(input.readUTF());

           // logger.log(Level.INFO, input.readUTF());

        }

    }
}