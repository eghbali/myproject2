package project2;

import java.io.Serializable;

/**
 * Created by DotinSchool2 on 10/21/2015.
 */
public class Transaction implements Serializable {

    String terminalId;
    String terminalType;
    String outLogPass;
    String serverIp;
    String port;
    String transactionId;
    String transactionType;
    Integer amount;
    String deposit;

    public Transaction(String inputTrminalId, String inputTerminalType, String inputOutLogPass, String inputServerIp, String inputPort, String inputTransactionId, String inputTtansactionType, int inputAmount, String inputDeposit) {

        terminalId = inputTrminalId;
        terminalType = inputTerminalType;
        outLogPass = inputOutLogPass;
        serverIp = inputServerIp;
        port = inputPort;
        transactionId = inputTransactionId;
        transactionType = inputTtansactionType;
        amount = inputAmount;
        deposit = inputDeposit;

    }

}
