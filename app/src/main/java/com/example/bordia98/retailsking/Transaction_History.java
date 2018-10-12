package com.example.bordia98.retailsking;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//class to display the transaction history
public class Transaction_History extends AppCompatActivity {

    RecyclerView recyclerView;
    Transact_Adapter adapter;
    List<transact_history> list;


    // starting the previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Billing.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction__history);

        // setting up external toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Transaction History");
        setSupportActionBar(toolbar);

        //initialising the recycler view
        recyclerView = findViewById(R.id.transactionList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();

        // opening the table storing transaction record
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select * from "+ transactions.Tablename;
        Cursor c = db.rawQuery(query,null);
        if(c!=null &&c.getCount()>0){
            c.moveToFirst();
            while (!c.isAfterLast()){
                // making a transact history object and adding to the list
                transact_history record = new transact_history(c.getString(0),String.valueOf(c.getFloat(1)));
                list.add(record);
                c.moveToNext();
            }

            Collections.reverse(list);
        }

        // assigning the transact adapter class
        adapter  = new Transact_Adapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);
    }

}
