package com.example.bordia98.retailsking;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;

public class Billing extends AppCompatActivity {

    Button Scan_product;
    String barnumber="";
    RecyclerView recyclerView;
    billingproductadapter adapter;
    List<Integer> quantitycounter;
    Button generate_invoice;
    List<billing_product> productList;
    EditText customermob,customername;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        StoreManagerDB_Helper helper10 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db10 = helper10.getReadableDatabase();

        int countentries=0;
        String query = "Select count(*) from " + tempcustomer.table_name;
        Cursor c10 = db10.rawQuery(query,null);
        if(c10!=null && c10.getCount()>0){
            c10.moveToFirst();
            countentries=c10.getInt(0);
        }
        if(countentries!=0){
            String[] listarray = new String[countentries];
            String query1 = "Select " +tempcustomer.product_barcode + " from "+ tempcustomer.table_name;
            Cursor c11 = db10.rawQuery(query1,null);
            if(c11!=null &&  c11.getCount()>0){
                c11.moveToFirst();
                int k=0;
                while( !c11.isAfterLast()){
                    listarray[k]=c11.getString(0);
                    c11.moveToNext();
                }
            }

            StoreManagerDB_Helper helper7 = new StoreManagerDB_Helper(getApplicationContext());
            SQLiteDatabase db = helper7.getWritableDatabase();

            for(int i=0;i<countentries;i++){
                db.delete(tempcustomer.table_name,tempcustomer.product_barcode+"=\""+listarray[i]+"\"",null);
            }
            db.close();
        }

        db10.close();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);


        StoreManagerDB_Helper helper10 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db10 = helper10.getReadableDatabase();

        int countentries=0;
        String query = "Select count(*) from " + tempcustomer.table_name;
        Cursor c10 = db10.rawQuery(query,null);
        if(c10!=null && c10.getCount()>0){
            c10.moveToFirst();
            countentries=c10.getInt(0);
        }
        if(countentries!=0){
            String[] listarray = new String[countentries];
            String query1 = "Select " +tempcustomer.product_barcode + " from "+ tempcustomer.table_name;
            Cursor c11 = db10.rawQuery(query1,null);
            if(c11!=null &&  c11.getCount()>0){
                c11.moveToFirst();
                int k=0;
                while( !c11.isAfterLast()){
                    listarray[k]=c11.getString(0);
                    c11.moveToNext();
                }
            }

            StoreManagerDB_Helper helper7 = new StoreManagerDB_Helper(getApplicationContext());
            SQLiteDatabase db = helper7.getWritableDatabase();

            for(int i=0;i<countentries;i++){
                db.delete(tempcustomer.table_name,tempcustomer.product_barcode+"=\""+listarray[i]+"\"",null);
            }
            db.close();
        }

        db10.close();

        finishAffinity();
        System.exit(0);

    }


    private boolean checkWriteExternalPermission()
    {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Scan_product.setEnabled(true)   ;
                    return;
                }else{
                    Toast.makeText(getApplicationContext(),"Please grant the permission first and restart the app",Toast.LENGTH_LONG).show();
                }
            }
            default:
                return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StoreManagerDB_Helper helper10 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db10 = helper10.getReadableDatabase();

        int countentries=0;
        String query = "Select count(*) from " + tempcustomer.table_name;
        Cursor c10 = db10.rawQuery(query,null);
        if(c10!=null && c10.getCount()>0){
            c10.moveToFirst();
            countentries=c10.getInt(0);
        }
        if(countentries!=0){
            String[] listarray = new String[countentries];
            String query1 = "Select " +tempcustomer.product_barcode + " from "+ tempcustomer.table_name;
            Cursor c11 = db10.rawQuery(query1,null);
            if(c11!=null &&  c11.getCount()>0){
                c11.moveToFirst();
                int k=0;
                while( !c11.isAfterLast()){
                    listarray[k]=c11.getString(0);
                    c11.moveToNext();
                }
            }

            StoreManagerDB_Helper helper7 = new StoreManagerDB_Helper(getApplicationContext());
            SQLiteDatabase db = helper7.getWritableDatabase();

            for(int i=0;i<countentries;i++){
                db.delete(tempcustomer.table_name,tempcustomer.product_barcode+"=\""+listarray[i]+"\"",null);
            }
            db.close();
        }

        db10.close();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Billing");
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.productlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productList=new ArrayList<>();
        quantitycounter = new ArrayList<>();
        Scan_product = findViewById(R.id.enter_product);
        generate_invoice = findViewById(R.id.Generate_invoice);

        customername = findViewById(R.id.customer_name);
        customermob = findViewById(R.id.customer_mobno);

        if(checkWriteExternalPermission()==true){
            Scan_product.setEnabled(true);
            //Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();
        }else{
            Scan_product.setEnabled(false);
            //Toast.makeText(getApplicationContext(),"Writing external application permission not granted",Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        generate_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                starttransition();

            }
        });
        Scan_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanbarcode();
            }
        });

    }

    private void starttransition() {

        String name = customername.getText().toString().trim();
        String mob = customermob.getText().toString();

        if(name.length()<=0){
            customername.setError("Please write customer name");
            customername.requestFocus();
            return;
        }

        if(mob.length()<=0){
            customermob.setError("Enter mobile number or customer");
            customermob.requestFocus();
            return;
        }

        if(mob.length()!=10){
            customermob.setError("Please enter a valid phone number");
            customermob.requestFocus();
            return;
        }

        StoreManagerDB_Helper helper11 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db11 = helper11.getReadableDatabase();

        int countentries=0;
        String query = "Select count(*) from " + tempcustomer.table_name;
        Cursor c0 = db11.rawQuery(query,null);
        if(c0!=null && c0.getCount()>0){
            c0.moveToFirst();
            countentries=c0.getInt(0);
        }
        if(countentries!=0) {
            String[] listarray = new String[countentries];
            String[] quantity = new String[countentries];
            String barcodeofall ="";
            String quantityall="";
            String query1 = "Select " + tempcustomer.product_barcode + "," + tempcustomer.product_purchase_quantity + " from " + tempcustomer.table_name;
            Cursor c1 = db11.rawQuery(query1, null);
            if (c1 != null && c1.getCount() > 0) {
                c1.moveToFirst();
                int k = 0;

                StoreManagerDB_Helper helper7 = new StoreManagerDB_Helper(getApplicationContext());
                SQLiteDatabase db = helper7.getWritableDatabase();



                while (!c1.isAfterLast()) {
                    listarray[k] = c1.getString(0);
                    Log.v("Entries of barcode",listarray[k]);
                    barcodeofall+=c1.getString(0);
                    db.delete(tempcustomer.table_name,tempcustomer.product_barcode+"=\""+c1.getString(0)+"\"",null);
                    barcodeofall+="#";
                    quantity[k] = String.valueOf(c1.getInt(1));
                    quantityall+=String.valueOf(c1.getInt(1));
                    quantityall+="#";
                    c1.moveToNext();
                }

                db.close();
                db11.close();

                quantitycounter=new ArrayList<>();
                Intent i = new Intent(getApplicationContext(), QR_Code.class);
                Bundle b=new Bundle();
                b.putStringArray("billingproduct",listarray);
                b.putStringArray("billingquantity",quantity);
                b.putString("allbarcodes",barcodeofall);
                b.putString("allquantity",quantityall);
                b.putString("custname", name);
                b.putString("custmob", mob);

                i.putExtras(b);
                startActivity(i);

            }
        }
    }

    private void scanbarcode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scaning code");
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(getApplicationContext(),"You cancelled Scanning",Toast.LENGTH_SHORT).show();
            }
            else{
                barnumber=result.getContents();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.profile:
            {
                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(i);
                return true;
            }
            case R.id.inventory:
            {
                Intent i = new Intent(getApplicationContext(),InventoryMainPage.class);
                startActivity(i);
                return true;
            }
            case R.id.transaction:{
                Intent i = new Intent(getApplicationContext(),Transaction_History.class);
                startActivity(i);
                return true;
            }
            case R.id.logout:{
                FirebaseAuth mauth = FirebaseAuth.getInstance();
                mauth.signOut();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        onResume();
        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.v("Product list ", productList.toString());
        adapter = new billingproductadapter(getApplicationContext(),productList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onResume(){
        super.onResume();

        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);


        if(barnumber.length()!=0){
            StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "Select * from retail_Store_Details where Product_Barcode=\"" + barnumber+"\"";
            Cursor c = db.rawQuery(query,null);
            if(c!=null && c.getCount()>0){

                c.moveToFirst();

                StoreManagerDB_Helper helper1 = new StoreManagerDB_Helper(getApplicationContext());
                SQLiteDatabase db1 = helper1.getReadableDatabase();
                String query2 = "Select count(*) from Temprecord";

                Cursor c2 = db1.rawQuery(query2,null);
                if(c2!=null  && c2.getCount()>0){
                    c2.moveToFirst();
                    int count;
                    count = c2.getInt(0);
                    if(count!=0){
                        String query1 = "Select * from Temprecord where "+tempcustomer.product_barcode+"=\"" + barnumber+"\"";
                        Cursor c1 = db1.rawQuery(query1,null);
                        if(c1!=null&&c1.getCount()>0){
                            c1.moveToFirst();
                            for(int i=0;i<productList.size();i++){
                                billing_product obj = productList.get(i);
                                Log.v("barcode of product",obj.getProduct_barcode());
                                if(obj.getProduct_barcode().equals(barnumber)){
                                    generate_invoice.setEnabled(true);
                                    if(quantitycounter.get(i)-(obj.getProduct_qty()+1)>=0){
                                        obj.setProduct_qty(obj.getProduct_qty()+1);

                                        StoreManagerDB_Helper helper2 = new StoreManagerDB_Helper(getApplicationContext());
                                        SQLiteDatabase db2 = helper2.getWritableDatabase();

                                        ContentValues val = new ContentValues();
                                        val.put(tempcustomer.product_barcode,barnumber);
                                        val.put(tempcustomer.product_purchase_quantity,obj.getProduct_qty());

                                        db2.update(tempcustomer.table_name,val,tempcustomer.product_barcode+"=\""+barnumber+"\"",null);

                                        db2.close();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Not enough product is present in stock",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
                            }
                        }else{
                            generate_invoice.setEnabled(true);
                            String pro = c.getString(1);
                            int qty = 1;
                            Float pri = c.getFloat(4);
                            billing_product product = new billing_product(pro, qty, pri, barnumber);
                            productList.add(product);

                            StoreManagerDB_Helper helper8 = new StoreManagerDB_Helper(getApplicationContext());
                            SQLiteDatabase db8 = helper8.getReadableDatabase();

                            String query8 = "select " + StoreTable.product_quantity + " from " +StoreTable.Table_Name+" where " + StoreTable.product_barcode + " =\"" + barnumber+"\"";
                            Cursor c8 = db8.rawQuery(query8,null);
                            c8.moveToFirst();
                            quantitycounter.add(c8.getInt(0));
                            c8.moveToNext();
                            db8.close();


                            StoreManagerDB_Helper helper3 = new StoreManagerDB_Helper(getApplicationContext());
                            SQLiteDatabase db3 = helper3.getWritableDatabase();

                            ContentValues val = new ContentValues();
                            val.put(tempcustomer.product_barcode,barnumber);
                            val.put(tempcustomer.product_purchase_quantity,qty);

                            db3.insert(tempcustomer.table_name,null,val);

                            db3.close();
                        }
                    }else{
                        generate_invoice.setEnabled(true);
                        String pro = c.getString(1);
                        int qty = 1;
                        Float pri = c.getFloat(4);
                        billing_product product = new billing_product(pro, qty, pri, barnumber);
                        productList.add(product);

                        StoreManagerDB_Helper helper8 = new StoreManagerDB_Helper(getApplicationContext());
                        SQLiteDatabase db8 = helper8.getReadableDatabase();

                        String query8 = "select " + StoreTable.product_quantity + " from " +StoreTable.Table_Name+" where " + StoreTable.product_barcode + "=\"" + barnumber+"\"";
                        Cursor c8 = db8.rawQuery(query8,null);
                        if(c8!=null && c8.getCount()>0){
                            c8.moveToFirst();
                            int cou =c8.getInt(0);
                            quantitycounter.add(cou);
                        }
                        db8.close();


                        StoreManagerDB_Helper helper4 = new StoreManagerDB_Helper(getApplicationContext());
                        SQLiteDatabase db4 = helper4.getWritableDatabase();

                        ContentValues val = new ContentValues();
                        val.put(tempcustomer.product_barcode,barnumber);
                        val.put(tempcustomer.product_purchase_quantity,qty);

                        db4.insert(tempcustomer.table_name,null,val);

                        db4.close();
                    }
                }
                else {
                    generate_invoice.setEnabled(true);
                    String pro = c.getString(1);
                    int qty = 1;
                    Float pri = c.getFloat(4);
                    billing_product product = new billing_product(pro, qty, pri, barnumber);
                    productList.add(product);



                    StoreManagerDB_Helper helper8 = new StoreManagerDB_Helper(getApplicationContext());
                    SQLiteDatabase db8 = helper8.getReadableDatabase();

                    String query8 = "select " + StoreTable.product_quantity + " from " +StoreTable.Table_Name+"  where " + StoreTable.product_barcode + "=\"" + barnumber;
                    Cursor c8 = db8.rawQuery(query8,null);
                    c8.moveToFirst();
                    quantitycounter.add(c8.getInt(0));
                    c8.moveToNext();
                    db8.close();


                    StoreManagerDB_Helper helper5 = new StoreManagerDB_Helper(getApplicationContext());
                    SQLiteDatabase db5 = helper5.getWritableDatabase();

                    ContentValues val = new ContentValues();
                    val.put(tempcustomer.product_barcode,barnumber);
                    val.put(tempcustomer.product_purchase_quantity,qty);

                    db5.insert(tempcustomer.table_name,null,val);

                    db5.close();

                }
                db1.close();
            }else{
                Toast.makeText(getApplicationContext(),"No such entry is available",Toast.LENGTH_SHORT).show();
            }
            db.close();
        }

        barnumber="";


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.billing_menu,menu);
        return true;
    }

}
