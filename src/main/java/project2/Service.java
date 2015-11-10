package project2;

import javax.sql.rowset.spi.TransactionalWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by DotinSchool2 on 11/4/2015.
 */
public class Service extends Thread {
    static ArrayList<Deposit> deposits;
    final Socket socket;
    Logger logger;


    public Service(ArrayList<Deposit> deposits, Socket socket) {
        this.deposits = deposits;
        this.socket = socket;
    }

    public static Logger makeLoggingFile() throws IOException {
        Handler fileHandler = new FileHandler("Server.log", true);
        Logger logger = Logger.getLogger(Service.class.getName());
        logger.addHandler(fileHandler);
        return logger;

    }

    public static int findTransactionTarget(Transaction transaction) {
        String depositId = transaction.deposit;

        for (int i = 0; i < deposits.size(); i++) {
            if ((deposits.get(i).id).equals(depositId)) {
                return i;
            }
        }

        return deposits.indexOf(transaction);
    }

    public void run() {
        try {
            //**************TEST***********************
//            if (Thread.currentThread().getName().equals("Thread-1")) {
//                Thread.currentThread().sleep(5000);
//            }
            logger = makeLoggingFile();
            logger.setUseParentHandlers(false);
            System.out.println(Thread.currentThread().getName() + "is running server");
            processTransactionsfromTerminal(socket);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("unable to make log File");
        }
        //***************TEST********************
//          catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    public void processTransactionsfromTerminal(Socket socket) {
        try {

            System.out.println(Thread.currentThread().getName() + "is running processTransactionsfromTerminal");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Transaction transaction;
            transaction = (Transaction) in.readObject();
            while (transaction != null) {
                System.out.println("serever received transaction from treminal " + transaction.terminalId + " transaction i:" + transaction.transactionId);
                logger.log(Level.INFO, "recieved Transaction -->transactionId:"
                        + transaction.transactionId + " Transaction type:" + transaction.transactionType + " terminalid:" + transaction.terminalId + "\n");
                int index = findTransactionTarget(transaction);
                boolean validated = completeTransaction(transaction, index);
                //   output.writeUTF("Transactionid " + transaction.transactionId + " succeeded\n");
                logger.log(Level.INFO, "Transactionid " + transaction.transactionId + " succeeded\n");
                System.out.println(Thread.currentThread().getName() + "  Transaction id: " + transaction.transactionId + "---->" + validated);
                transaction = (Transaction) in.readObject();

            }


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("unable to create objectInputStream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("unable to read object");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("InterruptedException");
        }


    }

    public boolean completeTransaction(Transaction transaction, int index) throws InterruptedException, DepositException {

        Deposit targetDeposit = deposits.get(index);
        BigDecimal transactionAmount = transaction.amount;
        synchronized (deposits.get(index)) {
            System.out.println(Thread.currentThread().getName() + " START");
            BigDecimal depositAmount = targetDeposit.initialBalance;
            //***************TEST************************
//            if (Thread.currentThread().getName().equals("Thread-0")) {
//                Thread.currentThread().sleep(30000);
//            }
            String type = transaction.transactionType;
            boolean result = false;
            if (type.equals("deposit")) {

                if ((transactionAmount.add(depositAmount)).compareTo(new BigDecimal(1000000)) >= 0) {
                    result = false;
                    System.err.println("UpperBound exception");

                } else {
                    deposits.get(deposits.indexOf(targetDeposit)).initialBalance = transactionAmount.add(depositAmount);
                    result = true;
                }
            } else {
                if ((depositAmount.compareTo(transactionAmount)) > 0) {
                    deposits.get(deposits.indexOf(targetDeposit)).initialBalance = depositAmount.subtract(transactionAmount);
                    result = true;
                } else {

                     System.err.println("Deposit Amount is not enough for withdraw"+transaction.transactionId);
                }
            }

            try {
                DataOutputStream outResult = new DataOutputStream(socket.getOutputStream());
                outResult.write(result ? 1 : 0);

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("socket problem, unable to send the transaction result");
            }
            System.out.println(Thread.currentThread().getName() + " END");
            return result;

        }
    }


}
