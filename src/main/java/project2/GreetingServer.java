package project2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GreetingServer extends Thread {
    private ServerSocket serverSocket;
    ArrayList<Deposit> deposits;
    int port;
    Logger logger;

    public GreetingServer() throws IOException {

    }


    public void run() {

        try {

            logger = makeLoggingFile();
            JsonParser jsonparser = new JsonParser();
            deposits = jsonparser.readFile();
            port = jsonparser.portvalue;
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);

        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {

                //logContent.append(cal.getTime()).append(" Server is Waiting for client on port ").append(serverSocket.getLocalPort()).append("\n");
                System.out.println("Server!");
                Socket server = serverSocket.accept();
                System.out.println("connect!");
                processTerminalRequests(server);



                 server.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } //catch (ClassNotFoundException e) {
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //   e.printStackTrace();
            //}
        }
    }


    private void processTerminalRequests(Socket server) throws IOException, ClassNotFoundException {
        String terminalId = processInputObjects( server);
        logger.log(Level.INFO,"end of connection to terminal:"+terminalId);
    }


    private String processInputObjects(Socket server) throws IOException, ClassNotFoundException {
        DataOutputStream output = new DataOutputStream(server.getOutputStream());
        DataInputStream input = new DataInputStream(server.getInputStream());
        int numberOfTransactions = input.readInt();
        String terminalId = input.readUTF();

        logger.log(Level.INFO,"Just connected to "+server.getRemoteSocketAddress()+" terminalId:"+terminalId+"\n");
        ObjectInputStream in = new ObjectInputStream(server.getInputStream());
        //  String terminalId=input.readUTF((DataInput) server.getInputStream());
        //khandane transaction haye ferestade shode az client
        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction transaction = (Transaction) in.readObject();
            System.out.println("serever received transaction from treminal "+transaction.terminalId+" transaction i:"+transaction.transactionId);
            logger.log(Level.INFO,"recieved Transaction -->transactionId:"
                    +transaction.transactionId+" Transaction type:"+transaction.transactionType+" terminalid:"+transaction.terminalId+"\n");
            Deposit deposit = findDeposit(deposits, transaction);
            boolean validated = checkValidity(transaction.amount, deposit.initialBalance, deposit.upperBound);

            if (validated) {
                deposits = calculate(deposits, transaction);
                output.writeUTF("Transactionid " + transaction.transactionId + " succeeded\n");
                logger.log(Level.INFO,"Transactionid "+transaction.transactionId+" succeeded\n");
            } else {

                logger.log(Level.INFO,"invalid  Transaction \n");
                output.writeUTF("Transactionid " + transaction.transactionId + " failed\n");

            }

            logger.log(Level.INFO,"end of transaction:"+transaction.transactionId+"\n");
            System.out.println("end of transaction from treminal "+transaction.terminalId+" transaction i:"+transaction.transactionId);
        }
        return terminalId;
    }


    public ArrayList<Deposit> calculate(ArrayList<Deposit> deposits, Transaction transaction) {
        String deposiId = transaction.deposit;
        Deposit targetDeposit = null;
        for (Deposit deposit : deposits) {
            if ((deposit.id).equals(deposiId)) {
                targetDeposit = deposit;
            }
        }

        int transactionAmount = transaction.amount;
        int depositAmount = targetDeposit.initialBalance;


        String type = transaction.transactionType;
    //    synchronized (this) {
            if (type.equals("deposit")) {
                if (transactionAmount + depositAmount >= 1000000) {

                } else {
                    deposits.get(deposits.indexOf(targetDeposit)).initialBalance = transactionAmount + depositAmount;
                }
            } else {
                if (depositAmount > transactionAmount) {
                    deposits.get(deposits.indexOf(targetDeposit)).initialBalance = depositAmount - transactionAmount;
                }
            }
      //  }
        //////////////////////////////////
        return deposits;
    }

    public static Deposit findDeposit(ArrayList<Deposit> deposits, Transaction transaction) {
        String deposiId = transaction.deposit;

        for (Deposit deposit : deposits) {
            if ((deposit.id).equals(deposiId)) {

                return deposit;
            }
        }
        return null;
    }

    public static boolean checkValidity(int depositAmount, int transactionAmount, int upperBound) {
        return depositAmount + transactionAmount <= upperBound;
    }

    public static Logger makeLoggingFile() throws IOException {
        Handler fileHandler = new FileHandler("Server.log",true);
        Logger logger = Logger.getLogger(GreetingServer.class.getName());
        logger.addHandler(fileHandler);
        return logger;

    }

}