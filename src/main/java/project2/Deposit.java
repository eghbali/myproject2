package project2;

import java.io.Serializable;

/**
 * Created by DotinSchool2 on 10/26/2015.
 */
public class Deposit implements Serializable {
    String customer;
    String id;
    int initialBalance;
    int upperBound;

    public Deposit(String customerName, String customerId, int customerInitialBalance, int upperBoundValue) {
        customer = customerName;
        id = customerId;
        initialBalance = customerInitialBalance;
        upperBound = upperBoundValue;

    }
}
