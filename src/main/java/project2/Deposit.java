package project2;

import java.io.Serializable;
import java.math.BigDecimal;

public class Deposit implements Serializable {
    String customer;
    String id;
    BigDecimal initialBalance;
    BigDecimal upperBound;

    public Deposit(String customerName, String customerId, BigDecimal customerInitialBalance, BigDecimal upperBoundValue) {
        customer = customerName;
        id = customerId;
        initialBalance = customerInitialBalance;
        upperBound = upperBoundValue;

    }
}
