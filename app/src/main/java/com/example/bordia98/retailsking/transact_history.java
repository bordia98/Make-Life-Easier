package com.example.bordia98.retailsking;

// object class for holding the object of the values to be displayed in recycler view
public class transact_history {

    String transactionnumber;
    String amount;

    public transact_history(String transactionnumber, String amount) {

        this.transactionnumber = transactionnumber;
        this.amount = amount;
    }

    public String getTransactionnumber() {
        return transactionnumber;
    }

    public String getAmount() {
        return amount;
    }


}
