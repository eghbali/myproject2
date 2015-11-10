package project2;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by DotinSchool2 on 10/21/2015.
 */
public class Transaction implements Serializable {

    String terminalId;
    String terminalType;
    String outLogPass;
    String serverIp;
    int port;
    String transactionId;
    String transactionType;
    BigDecimal amount;
    String deposit;

    public Transaction(String terminalId, String terminalType, String outLogPass, String serverIp, int port, String transactionId,
                       String transactionType, BigDecimal amount, String deposit) {

        this.terminalId = terminalId;
        this.terminalType = terminalType;
        this.outLogPass = outLogPass;
        this.serverIp = serverIp;
        this.port = port;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.deposit = deposit;

    }

}
