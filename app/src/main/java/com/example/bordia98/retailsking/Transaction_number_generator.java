package com.example.bordia98.retailsking;

import java.text.SimpleDateFormat;
import java.util.Date;

// class for generating transaction number

public class Transaction_number_generator {

    String retailer_id;
    String organisation_id;
    String unit_id;


    public Transaction_number_generator(String retailer_id, String organisation_id, String unit_id) {
        this.retailer_id = retailer_id;
        this.organisation_id = organisation_id;
        this.unit_id = unit_id;
    }

    public String Generate_Transaction_No(int number){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String ans="";
        int num = number%10000000;
        String add = String.valueOf(num);
        while(add.length()!=7){
            add = '0'+add;
        }
        ans+=retailer_id+"-"+organisation_id+"-"+unit_id+"-"+currentDateandTime+"-" + add;
        return  ans;
    }

}
