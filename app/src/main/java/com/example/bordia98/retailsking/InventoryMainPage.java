package com.example.bordia98.retailsking;


import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class InventoryMainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InsertInventoryFragment.insertclicklistner,SearchInventoryFragment.searchclicklistner,EditInventoryFragment.editclicklistner , DeleteInventoryFragment.deleteclicklistner  {
    static Toolbar toolbar;

    public static void setTitle(String Title){
        toolbar.setTitle(Title);
    }

    protected static String barnumber="";

    NavigationView navigationView;
    InsertInventoryFragment obj;



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
            case R.id.billing:{
                Intent i = new Intent(getApplicationContext(),Billing.class);
                startActivity(i);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory_menu,menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);


        setContentView(R.layout.activity_inventory_main_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Inventory");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame,new HomeInventoryFragment());
        ft.commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        if(Build.VERSION.SDK_INT>=27)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.home){
            Log.v("Home","inside home inventory");
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,new HomeInventoryFragment());
            ft.commit();
        }
        if(id==R.id.insert){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            InsertInventoryFragment fragment = new InsertInventoryFragment();
            ft.replace(R.id.mainFrame,fragment);
            ft.commit();
        }
        if(id==R.id.edit){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,new EditInventoryFragment());
            ft.commit();
        }
        if(id==R.id.delete){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,new DeleteInventoryFragment());
            ft.commit();
        }
        if(id==R.id.search){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,new SearchInventoryFragment());
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void scanbarcode(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scaning code");
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.initiateScan();
    }

    @Override
    public void savetheproduct(String proname, String numbers, Float pr, int qty,Float gstpercent,Float discount) {

        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select * from retail_Store_Details where Product_Barcode=\"" + numbers+"\"";
        Cursor c = db.rawQuery(query,null);
        if(c!=null && c.getCount()>0){
            int quantity=0;
            c.moveToFirst();
            while(!c.isAfterLast()){
                quantity = c.getInt(2);
                c.moveToNext();
            }
            qty+=quantity;
            db.close();

            db = helper.getWritableDatabase();
            ContentValues val = new ContentValues();
            val.put(StoreTable.product_barcode,numbers);
            val.put(StoreTable.product_name,proname);
            val.put(StoreTable.product_price,pr);
            val.put(StoreTable.product_quantity,qty);
            val.put(StoreTable.product_gst,gstpercent);
            val.put(StoreTable.product_discount,discount);
            db.update(StoreTable.Table_Name,val,"Product_Barcode=\""+numbers+"\"",null);
            db.close();
            Toast.makeText(getApplicationContext(),"Product Details is updated in database",Toast.LENGTH_SHORT).show();

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,new HomeInventoryFragment());
            ft.commit();
            navigationView.setCheckedItem(R.id.home);


        }else{
            db.close();
            db = helper.getWritableDatabase();
            ContentValues val = new ContentValues();
            val.put(StoreTable.product_barcode,numbers);
            val.put(StoreTable.product_name,proname);
            val.put(StoreTable.product_price,pr);
            val.put(StoreTable.product_quantity,qty);
            val.put(StoreTable.product_gst,gstpercent);
            val.put(StoreTable.product_discount,discount);
            db.insert(StoreTable.Table_Name,null,val);
            db.close();
            Toast.makeText(getApplicationContext(),"Product is saved in database",Toast.LENGTH_SHORT).show();

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame,new HomeInventoryFragment());
            ft.commit();
            navigationView.setCheckedItem(R.id.home);

        }
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
    public void scancodeforsearch(View v) {
        scanbarcode(v);
    }

    @Override
    public void scaneditbarcode(View v) {
        scanbarcode(v);
    }

    @Override
    public void updateproductdetails(String proname, String numbers, Float pr, int qty,Float gstper,Float discount) {
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(StoreTable.product_barcode,numbers);
        val.put(StoreTable.product_name,proname);
        val.put(StoreTable.product_price,pr);
        val.put(StoreTable.product_quantity,qty);
        val.put(StoreTable.product_gst,gstper);
        val.put(StoreTable.product_discount,discount);

        Log.v("Price which i updated",pr+"");
        int i = db.update(StoreTable.Table_Name,val,"Product_Barcode=\""+numbers+"\"",null);
        Log.v("Return status of update",i+"");
        db.close();
        Toast.makeText(getApplicationContext(),"Values updated in the database",Toast.LENGTH_SHORT).show();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame,new HomeInventoryFragment());
        ft.commit();
        navigationView.setCheckedItem(R.id.home);

    }

    @Override
    public void scancodefordelete(View v) {
        scanbarcode(v);
    }

    @Override
    public void deleterecord(String barcodenumber) {

        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(StoreTable.Table_Name,"Product_Barcode=\""+barcodenumber+"\"",null);
        db.close();
        Toast.makeText(getApplicationContext(),"Record is deleted successfully",Toast.LENGTH_SHORT).show();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame,new HomeInventoryFragment());
        ft.commit();
        navigationView.setCheckedItem(R.id.home);
    }
}