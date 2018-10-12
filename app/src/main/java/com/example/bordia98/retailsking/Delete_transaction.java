package com.example.bordia98.retailsking;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Delete_transaction extends AppCompatActivity {

    String amt,tno;
    TextView amount,transactionno;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Transaction_History.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_transaction);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Delete Transaction");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        amt = getIntent().getStringExtra("amt");
        tno = getIntent().getStringExtra("tno");

        amount = findViewById(R.id.amount);
        transactionno = findViewById(R.id.transaction_number);

        amount.setText(amt);
        transactionno.setText(tno);

        Button yes = findViewById(R.id.yes);
        Button no = findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Transaction_History.class);
                startActivity(i);
            }
        });
    }

    private void delete() {
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(transactions.Tablename,transactions.transaction_number+"=\""+tno+"\"",null);
        db.close();

        Intent i = new Intent(getApplicationContext(),Transaction_History.class);
        startActivity(i);
    }

}
