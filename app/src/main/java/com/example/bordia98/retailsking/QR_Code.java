package com.example.bordia98.retailsking;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

public class QR_Code extends AppCompatActivity {

    Bundle b;
    String[] billingproduct,productqty;
    String custname,custmob,transactno,barcodes,quantities;
    ImageView holder;
    TextView des;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "You Can't go back from this activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__code);

        b = this.getIntent().getExtras();
        barcodes = b.getString("allbarcodes");
        quantities = b.getString("allquantity");
        billingproduct = barcodes.split("#");
        productqty = quantities.split("#");
        custname = b.getString("custname");
        custmob = b.getString("custmob");
        holder = findViewById(R.id.holder);
        des = findViewById(R.id.descrip);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Payments");
        setSupportActionBar(toolbar);

        Button generateinvoice =  findViewById(R.id.Generate_invoice);
        Button cash = findViewById(R.id.cash);
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setImageResource(R.drawable.cash);
                des.setText("Payment Done In Cash");
            }
        });
        
        Button QR = findViewById(R.id.qrcode);
        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatecode();
            }
        });

        generateinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starttransition();
            }
        });

    }

    private void generatecode() {
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select * from Personal_Details";
        Cursor c = db.rawQuery(query,null);
        String upi = "9611590381@upi";
        c.moveToFirst();
        while (!c.isAfterLast()){
            upi = c.getString(7);
            c.moveToNext();
        }
        db.close();


        QRCodeWriter write = new QRCodeWriter();
        try{
            BitMatrix bitMatrix = write.encode(upi, BarcodeFormat.QR_CODE,512,512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            holder.setImageBitmap(bmp);
            des.setText("Please Scan The QR-Code For Payment");
        }catch (Exception e){
            System.out.print(e);
        }

    }

    private void starttransition() {
            Intent i = new Intent(getApplicationContext(), Invoice_Generation.class);
            Bundle b=new Bundle();
            b.putString("allbarcodes",barcodes);
            b.putString("allquantity",quantities);
            b.putString("custname", custname);
            b.putString("custmob", custmob);
            i.putExtras(b);
            startActivity(i);
    }

}
