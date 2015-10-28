package project2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static project2.TransactionHandler.*;


/**
 * Created by DotinSchool2 on 10/20/2015.
 */
public class GreetingServer extends Thread {
    private ServerSocket serverSocket;
    ArrayList<Deposit> deposits;

    public GreetingServer(int port, ArrayList<Deposit> deposits) throws IOException {
        this.deposits = deposits;
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {

        FileWriter file = null;


        while (true) {
            try {

                StringBuilder logContent = new StringBuilder();
                logContent.append((new Date()).toString()).append(" Server is Waiting for client on port ").append(serverSocket.getLocalPort()).append("\n");
                Socket server = serverSocket.accept();
               System.out.println( this.getName());
                processTerminalRequests(logContent, server);
                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void processTerminalRequests(StringBuilder logContent, Socket server) throws IOException, ClassNotFoundException {
        FileWriter file;
        file = new FileWriter("serverlog.txt", true);
        String terminalId = processInputObjects(logContent, server);

        logContent.append("end of connection to terminal:").append(terminalId);
        file.write(logContent.toString());
        file.close();
    }

    private String processInputObjects(StringBuilder logContent, Socket server) throws IOException, ClassNotFoundException {
        DataOutputStream output = new DataOutputStream(server.getOutputStream());
        DataInputStream input = new DataInputStream(server.getInputStream());
        int numberOfTransactions = input.readInt();
        String terminalId = input.readUTF();
        logContent.append("Just connected to ").append(server.getRemoteSocketAddress())
                .append(" terminalId:").append(terminalId).append("\n");
        ObjectInputStream in = new ObjectInputStream(server.getInputStream());
        //  String terminalId=input.readUTF((DataInput) server.getInputStream());
        //khandane transaction haye ferestade shode az client

        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction transaction = (Transaction) in.readObject();
            logContent.append("recieved Transaction -->transactionId:")
                    .append(transaction.transactionId).append(" Transaction type:")
                    .append(transaction.transactionType).append(" terminalid:").append(transaction.terminalId).append("\n");
            Deposit deposit = findDeposit(deposits, transaction);
            boolean validated = checkValidity(transaction.amount, deposit.initialBalance, deposit.upperBound);
            if (validated) {

                deposits = calculate(deposits, transaction);
                logContent.append("Transactionid ").append(transaction.transactionId).append(" succeeded\n");
                output.writeUTF("Transactionid " + transaction.transactionId + " succeeded\n");
            } else {
                logContent.append("invalid  Transaction \n");
                output.writeUTF("Transactionid " + transaction.transactionId + " failed\n");

            }
            logContent.append("end of transaction:").append(transaction.transactionId).append("\n");
        }
        return terminalId;
    }

    public static void main(String[] args) throws IOException {

        JsonParser jsonparser = new JsonParser();
        ArrayList<Deposit> deposits = jsonparser.readFile();
        int port = jsonparser.portvalue;
        Thread server = new Thread(new GreetingServer(port, deposits));
        Thread client = new Thread(new GreetingClient(), "name1");
        Thread client2 = new Thread(new GreetingClient(), "name2");

        server.start();
        client.start();
        client2.start();


    }
}