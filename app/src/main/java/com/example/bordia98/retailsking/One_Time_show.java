package com.example.bordia98.retailsking;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Activity which will register the user for the very first time
public class One_Time_show extends AppCompatActivity {

    Button submit;
    EditText owner_name,shop_name,upi_id,mob_no,gst_no,retail_id,business_id,org_id;

    // handling the events when back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one__time_show);

        owner_name = findViewById(R.id.owner_name);
        shop_name = findViewById(R.id.shop_name);
        mob_no = findViewById(R.id.mobno);
        gst_no = findViewById(R.id.gst_number);
        retail_id=findViewById(R.id.retail_id);
        business_id=findViewById(R.id.businessunit_id);
        org_id = findViewById(R.id.organisation_id);
        upi_id = findViewById(R.id.upi);
        submit = findViewById(R.id.submit_details);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveandproceed();
            }
        });
    }

    //function to save the details of the user in the table StoreOwner
    private void saveandproceed() {
        String name = owner_name.getText().toString().trim();
        String shopname = shop_name.getText().toString().trim();
        String mobno = mob_no.getText().toString().trim();
        String gstid  = gst_no.getText().toString().trim();
        String retid= retail_id.getText().toString().trim();
        String orgid = org_id.getText().toString().trim();
        String busiunit = business_id.getText().toString().trim();
        String upi = upi_id.getText().toString().trim();

        String id="1";
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

        StoreManagerDB_Helper helper1 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db1 = helper1.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StoreOwner.ownername,name);
        values.put(StoreOwner.shopname,shopname);
        values.put(StoreOwner.owner_phoneno,mobno);
        values.put(StoreOwner.GSTIN_number,gstid);
        values.put(StoreOwner.Retail_id,retid);
        values.put(StoreOwner.bussinessunit_id,busiunit);
        values.put(StoreOwner.Organisation_id,orgid);
        values.put(StoreOwner.id,id);
        values.put(StoreOwner.UPI_id,upi);

        db1.insert("Personal_Details",null,values);
        db1.close();

        Toast.makeText(getApplicationContext(),"Owner's Details updated in the database",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

}
