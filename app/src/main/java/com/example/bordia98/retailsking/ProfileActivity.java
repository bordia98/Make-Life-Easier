package com.example.bordia98.retailsking;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Activity to display the profile of the user which is registered and providing update option to it

public class ProfileActivity extends AppCompatActivity {

    Button submit;
    EditText owner_name,shop_name,mob_no,gst_no,retail_id,business_id,org_id,upi_id;
    String name,shopname,mobno,gstid,retid,busiunit,orgid,upi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        owner_name = findViewById(R.id.owner_name);
        shop_name = findViewById(R.id.shop_name);
        mob_no = findViewById(R.id.mobno);
        gst_no = findViewById(R.id.gst_number);
        retail_id=findViewById(R.id.retail_id);
        upi_id = findViewById(R.id.upi);
        business_id=findViewById(R.id.businessunit_id);
        org_id = findViewById(R.id.organisation_id);
        submit = findViewById(R.id.submit_details);

        //setting up the supported toolbar

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        // loading the personal details of the user which is already registered and setting them in placeholders
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select * from Personal_Details";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            name = c.getString(0);
            shopname=c.getString(1);
            mobno=c.getString(2);
            gstid=c.getString(3);
            retid = c.getString(4);
            orgid = c.getString(5);
            busiunit = c.getString(6);
            upi = c.getString(7);

            c.moveToNext();

        }
        db.close();

        owner_name.setText(name);
        shop_name.setText(shopname);
        mob_no.setText(mobno);
        gst_no.setText(gstid);
        retail_id.setText(retid);
        org_id.setText(orgid);
        business_id.setText(busiunit);
        upi_id.setText(upi);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateandsave();
            }
        });
    }

    // update option for updating the values in the database
    private void updateandsave() {
        name = owner_name.getText().toString().trim();
        shopname=shop_name.getText().toString().trim();
        mobno=mob_no.getText().toString().trim();
        gstid=gst_no.getText().toString().trim();
        retid= retail_id.getText().toString().trim();
        orgid = org_id.getText().toString().trim();
        busiunit = business_id.getText().toString().trim();
        upi = upi_id.getText().toString().trim();

        // constraint check for the correct entries
        if(name.length()==0){
            owner_name.setError("Name of the owner can't be empty");
            owner_name.requestFocus();
            return;
        }

        if (shopname.length()==0){
            shop_name.setError("Shop name can't be empty");
            owner_name.requestFocus();
            return;
        }

        if (mobno.length()!=10){
            mob_no.setError("Enter valid 10 digit mobile number");
            mob_no.requestFocus();
            return;
        }

        if(gstid.length()==0){
            gst_no.setError("Enter a valid gstid");
            gst_no.requestFocus();
            return;
        }

        if(retid.length()==0){
            retail_id.setError("Enter  a valid retail id");
            retail_id.requestFocus();
            return;
        }

        if(orgid.length()==0){
            org_id.setError("Enter a valid organisation id");
            org_id.requestFocus();
            return;
        }

        if(busiunit.length()==0){
            business_id.setError("Enter a valid businessunit");
            business_id.requestFocus();
            return;
        }


        if(upi.length()==0){
            upi_id.setError("Enter a valid upi");
            upi_id.requestFocus();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(StoreOwner.ownername,name);
        values.put(StoreOwner.shopname,shopname);
        values.put(StoreOwner.owner_phoneno,mobno);
        values.put(StoreOwner.GSTIN_number,gstid);
        values.put(StoreOwner.id,"1");
        values.put(StoreOwner.Retail_id,retid);
        values.put(StoreOwner.bussinessunit_id,busiunit);
        values.put(StoreOwner.Organisation_id,orgid);
        values.put(StoreOwner.UPI_id,upi);

        StoreManagerDB_Helper helper1 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db1 = helper1.getWritableDatabase();

        int i = db1.update("Personal_Details",values,"Id = \"1\"",null);
        Log.v("Database updated",i+"");
        Toast.makeText(getApplicationContext(),"Database updated",Toast.LENGTH_SHORT).show();
        db1.close();

        Intent in = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);

    }
}
