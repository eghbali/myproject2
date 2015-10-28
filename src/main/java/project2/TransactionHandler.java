package project2;

import java.util.ArrayList;

/**
 * Created by DotinSchool2 on 10/26/2015.
 */
public class TransactionHandler {

    public static ArrayList<Deposit> calculate(ArrayList<Deposit> deposits, Transaction transaction) {
        String deposiId = transaction.deposit;
        Deposit targetDeposit = null;
        for (Deposit deposit : deposits) {
            if ((deposit.id).equals(deposiId)) {
                targetDeposit = deposit;
            }
        }

        int transactionAmount = transaction.amount;
        int depositAmount = targetDeposit.initialBalance;
        int upperBalance = targetDeposit.upperBound;
        Boolean validity = true;
        String type = transaction.transactionType;
        if (type.equals("deposit")) {
            if (transactionAmount + depositAmount >= 1000000) {
                validity = false;
            } else {
                deposits.get(deposits.indexOf(targetDeposit)).initialBalance = transactionAmount + depositAmount;
            }
        } else {
            if (type.equals("withdrow")) {
                if (depositAmount < transactionAmount) {
                    validity = false;
                } else {
                    deposits.get(deposits.indexOf(targetDeposit)).initialBalance = depositAmount - transactionAmount;
                }
            }
        }

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
        if (depositAmount + transactionAmount > upperBound) {
            return false;
        } else return true;
    }
}

