package com.example.bordia98.retailsking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private Boolean firstTime = null;
    ImageView inventory,billingimageview;
    private FirebaseAuth mAuth;

    // handling on back pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            if(!(user.isEmailVerified())){
                Intent i = new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(i);
            }else{
                System.out.print("verified and logged in ");
            }
        }else{
            Intent i = new Intent(getApplicationContext(),Login_Activity.class);
            startActivity(i);
        }
    }

    // for displaying the icons of the menu in toolbar
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
        getMenuInflater().inflate(R.menu.menuhomepage,menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onStart();
        //checking whether it is opened first time or not
        isFirstTime();

        //checking whether there is any record of the owner in the database or not
        String val="";
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select Shop_name from Personal_Details";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            val = c.getString(0);
            c.moveToNext();
        }


        //if no record then show one time activity
        db.close();
        if(firstTime || val.length()==0){
            Intent i = new Intent(getApplicationContext(),One_Time_show.class);
            startActivity(i);
        }

        // checking wether there is product present in inventory or not
        // if no product present the launch this
        // if product present launch billing class

        db = helper.getReadableDatabase();
        String query1 ="Select count(*) from retail_Store_Details";
        Cursor c1 = db.rawQuery(query1,null);
        if(c1!=null) {
            c1.moveToFirst();
            int vali=0;

            while (!c1.isAfterLast()) {
                vali = c1.getInt(0);
                c1.moveToNext();
            }

            if(vali!=0){
                Intent i = new Intent(getApplicationContext(),Billing.class);
                startActivity(i);
            }
        }
        db.close();

        // setting up the toolbar
        setContentView(R.layout.activity_main);
        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle(val);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(bar);
        billingimageview = findViewById(R.id.billing);
        // billing class opening from imageview
        billingimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Billing.class);
                startActivity(i);
            }
        });
        // inventory page open
        inventory = findViewById(R.id.inventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),InventoryMainPage.class);
                startActivity(i);
            }
        });
    }

    // checking if the activity is launched for the first time returns True if first time else returns false
    private boolean isFirstTime(){
        if(firstTime==null){
            SharedPreferences preferences = this.getSharedPreferences("One_Time_Show", Context.MODE_PRIVATE);
            firstTime=  preferences.getBoolean("firstTime",true);
            if(firstTime){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstTime",false);
                editor.commit();
            }
        }
        return firstTime;
    }
}
