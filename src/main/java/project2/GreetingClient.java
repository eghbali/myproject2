package project2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by DotinSchool2 on 10/20/2015.
 */

public class GreetingClient implements Runnable {
    String serverName = "localhost";
    int port;

    public GreetingClient() {
        port = 123;
    }

    @Override
    public void run() {

        try {
            Calendar cal = Calendar.getInstance();
            //  System.out.println("  the name is*************" + this.getClass().getName());
            //file.write("Connecting to " + serverName + " on port " + port + " Current time is : " + cal.getTime() + "\n");
            Socket client = new Socket(serverName, port);
            //khandane file terminal va sakhtane liste transaction ha
            FileWriter file = sendTerminalTransactions(client);

            client.close();

            file.write("end of connection to " + client.getRemoteSocketAddress() + cal.getTime() + "\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileWriter sendTerminalTransactions(Socket client) throws IOException {
        Calendar cal = Calendar.getInstance();
        XmlParser FileReader = new XmlParser("input.xml");
        ArrayList<Transaction> transactions = FileReader.readXmlFile();
        FileWriter file = new FileWriter("terminallog.txt", true);
        file.write("Just connected to " + client.getRemoteSocketAddress() + cal.getTime() + "\n");
        DataInputStream input = new DataInputStream(client.getInputStream());
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        output.writeInt(transactions.size());
        output.writeUTF(transactions.get(0).terminalId);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        for (int i = 0; i < transactions.size(); i++) {
            file.write("sending transaction---> transactionid:" + transactions.get(i).transactionId + "\n");
            out.writeObject(transactions.get(i));
            //System.out.println(input.readUTF());
            file.write(input.readUTF());
        }
        return file;
    }

    public static ArrayList<Transaction> createTransactionsList() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        XmlParser transactionReader = new XmlParser("terminal.xml");
        transactionReader.readXmlFile();
        return transactions;
    }

}