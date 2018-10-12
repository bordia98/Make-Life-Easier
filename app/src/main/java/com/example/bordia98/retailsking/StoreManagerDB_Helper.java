package com.example.bordia98.retailsking;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// This is helper class to create the database with the table tempcustomer , storeowner , storetable and transactions

public class StoreManagerDB_Helper  extends SQLiteOpenHelper{

    private static final String Database_Name = "Retail_Store";
    private static final int Database_Version = 1;

    static String SQL_CREATE_STORE_TABLE;
    static String personal_details_table,tempcust,transact;
    Context context;

    public StoreManagerDB_Helper(Context context){
        super(context,Database_Name,null,Database_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating store table with barcode as the primary key
        if (StoreTable.Table_Name!=null) {
            SQL_CREATE_STORE_TABLE = "Create table " + StoreTable.Table_Name + " (" +
                    StoreTable.product_barcode + " TEXT PRIMARY KEY, " +
                    StoreTable.product_name + " TEXT, " +
                    StoreTable.product_quantity + " INTEGER, " +
                    StoreTable.product_gst + " FLOAT(10,2), " +
                    StoreTable.product_price + " FLOAT(10,2), " +
                    StoreTable.product_discount + " FLOAT(4,2) " + ");";

            db.execSQL(SQL_CREATE_STORE_TABLE);
            Log.v("Database created", "store Table");
        }
        Log.v("Passed","Table name is null");

        // creating store owner table with id of it as primary key by default it is always one
        // later one can change it and pass the update
        if(StoreOwner.table_name!=null){
            personal_details_table = "Create table " + StoreOwner.table_name + " ("+
                    StoreOwner.ownername + " TEXT, "+
                    StoreOwner.shopname + " TEXT, " +
                    StoreOwner.owner_phoneno + " TEXT, "+
                    StoreOwner.GSTIN_number + " TEXT, " +
                    StoreOwner.Retail_id + " TEXT, "+
                    StoreOwner.Organisation_id + " TEXT, "+
                    StoreOwner.bussinessunit_id + " TEXT, "+
                    StoreOwner.UPI_id + " TEXT, "+
                    StoreOwner.id+" TEXT PRIMARY KEY "+
                    " );";
            db.execSQL(personal_details_table);
            Log.v("Personal_details","created");
        }

        // creating tempcust table with product barcode as the primary key it will make the table
        // which will store the current purchase items helpful in bill generation
        if(tempcustomer.table_name!=null){
            tempcust="Create table " + tempcustomer.table_name + " ( "+
                    tempcustomer.product_barcode + " TEXT PRIMARY KEY, " +
                    tempcustomer.product_purchase_quantity + " INTEGER " +" );";
            db.execSQL(tempcust);
        }

        //transaction table to hold up the transactions

        if(transactions.Tablename!=null){
            transact="Create table "+ transactions.Tablename + " ( " +
                    transactions.transaction_number + " TEXT PRIMARY KEY, " +
                    transactions.transaction_amount + " FLOAT(10,2) " + " );";
            db.execSQL(transact);
        }
        Log.v("Passed","Personal details table name is null");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
