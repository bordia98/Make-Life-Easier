package com.example.bordia98.retailsking;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

// activity which generates invoice
public class Invoice_Generation extends AppCompatActivity {


    TableLayout bill_table;

    Bundle b;
    String[] billingproduct,productqty;
    String custname,custmob,transactno;
    float tot=0;
    float taxtot=0;
    float distot=0;
    TableLayout totallayout;
    TextView custn ;
    TextView custm ;
    TextView dealn ;
    TextView dealm ;
    TextView shop ;
    TextView dealg;
    Button share;

    // handling back click
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Billing.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice__generation);

        //getting the details from the previous activity
        b = this.getIntent().getExtras();
        billingproduct = b.getString("allbarcodes").split("#");
        productqty = b.getString("allquantity").split("#");
        custname = b.getString("custname");
        custmob = b.getString("custmob");

        share = findViewById(R.id.share);

        // checking for the functionality of the sharing button
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        // setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Invoice");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        custn = findViewById(R.id.custname);
        custm = findViewById(R.id.custmob);
        dealn = findViewById(R.id.dealname);
        dealm = findViewById(R.id.dealmob);
        shop = findViewById(R.id.shop);
        dealg = findViewById(R.id.dealgst);
        custn.setText(custname);
        custm.setText(custmob);

        String retid,orgid,busid;
        retid="";
        orgid="";
        busid="";

        //setting up the details like name and mobile no.
        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select * from Personal_Details";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            dealn.setText(c.getString(0));
            dealm.setText(c.getString(2));
            shop.setText(c.getString(1));
            dealg.setText(c.getString(3));
            retid=c.getString(4);
            orgid=c.getString(5);
            busid=c.getString(6);
            c.moveToNext();
        }
        db.close();


        // generating the transaction number for the transaction and printing in invoice

        Transaction_number_generator generator = new Transaction_number_generator(retid,orgid,busid);
        StoreManagerDB_Helper helper5 = new StoreManagerDB_Helper(getApplicationContext());
        SQLiteDatabase db5 = helper5.getReadableDatabase();
        String query5 = "Select count(*) from " + transactions.Tablename;
        Cursor c5 = db5.rawQuery(query5,null);
        c5.moveToFirst();
        int jail = c5.getInt(0);
        db5.close();
        transactno = generator.Generate_Transaction_No(jail);


        bill_table = findViewById(R.id.billingtable);


        //option enabling sharing button
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareit();
            }
        });

        // making up the billing table and dynamically adding up the details

        for(int i=0;i<billingproduct.length;i++){
            StoreManagerDB_Helper helper1 = new StoreManagerDB_Helper(getApplicationContext());
            SQLiteDatabase db1 = helper1.getReadableDatabase();
            String query1 = "Select * from "+StoreTable.Table_Name+" where Product_Barcode=\"" + billingproduct[i]+"\"";
            Cursor c1 = db1.rawQuery(query1,null);
            if(c1!=null && c1.getCount()>0){
                c1.moveToFirst();
                while(!c1.isAfterLast()){
                    String pro = c1.getString(1);
                    Float gstper = c1.getFloat(3);
                    Float pri = c1.getFloat(4);
                    Float dis = c1.getFloat(5);
                    int qty = Integer.valueOf(productqty[i]);



                    StoreManagerDB_Helper helper13 = new StoreManagerDB_Helper(getApplicationContext());
                    SQLiteDatabase db13 = helper13.getWritableDatabase();
                    ContentValues val = new ContentValues();
                    if(c1.getInt(2)-qty>0){
                        val.put(StoreTable.product_barcode,billingproduct[i]);
                        val.put(StoreTable.product_name,pro);
                        val.put(StoreTable.product_price,pri);
                        val.put(StoreTable.product_gst,gstper);
                        val.put(StoreTable.product_discount,dis);
                        val.put(StoreTable.product_quantity, (c1.getInt(2)-qty));
                        int j= db13.update(StoreTable.Table_Name,val,"Product_Barcode=\""+billingproduct[i]+"\"",null);
                        Log.v("Return status of update",j+"");
                    }else{
                        db13.delete(StoreTable.Table_Name,"Product_Barcode=\""+billingproduct[i]+"\"",null);
                    }
                    db13.close();




                    TableRow row = new TableRow(this);

                    row.setLayoutParams(new TableLayout.LayoutParams(bill_table.getLayoutParams()));
                    row.setBackgroundResource(R.drawable.row_border);
                    TextView tv1 = new TextView(this);
                    tv1.setText(String.valueOf(i+1));
                    tv1.setGravity(Gravity.CENTER);
                    tv1.setTextColor(Color.parseColor("#303F9F"));
                    tv1.setPadding(3,3,3,3);
                    tv1.setBackgroundResource(R.drawable.row_border);
                    row.addView(tv1);


                    TextView tv2 = new TextView(this);
                    tv2.setText(pro);
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setPadding(3,3,3,3);
                    tv2.setTextColor(Color.parseColor("#303F9F"));
                    tv2.setBackgroundResource(R.drawable.row_border);
                    row.addView(tv2);

                    TextView tv8 = new TextView(this);
                    tv8.setText(productqty[i]);
                    tv8.setBackgroundResource(R.drawable.row_border);
                    tv8.setGravity(Gravity.CENTER);
                    tv8.setTextColor(Color.parseColor("#303F9F"));
                    tv8.setPadding(3,3,3,3);
                    row.addView(tv8);


                    TextView tv3 = new TextView(this);
                    float mrp = pri - (gstper*pri/100);
                    tv3.setText(String.valueOf(mrp));
                    tv3.setBackgroundResource(R.drawable.row_border);
                    tv3.setPadding(3,3,3,3);
                    tv3.setGravity(Gravity.CENTER);
                    tv3.setTextColor(Color.parseColor("#303F9F"));
                    row.addView(tv3);


                    Float tax = (gstper*pri/200)*qty;
                    taxtot+=tax;
                    TextView tv4 = new TextView(this);
                    tv4.setText(String.valueOf(tax));
                    tv4.setPadding(3,3,3,3);
                    tv4.setBackgroundResource(R.drawable.row_border);
                    tv4.setGravity(Gravity.CENTER);
                    tv4.setTextColor(Color.parseColor("#303F9F"));
                    row.addView(tv4);

                    TextView tv5 = new TextView(this);
                    tv5.setText(String.valueOf(tax));
                    tv5.setPadding(3,3,3,3);
                    tv5.setGravity(Gravity.CENTER);
                    tv5.setBackgroundResource(R.drawable.row_border);
                    tv5.setTextColor(Color.parseColor("#303F9F"));
                    row.addView(tv5);

                    Float discountamout = mrp*dis*qty/100;
                    distot+=discountamout;
                    TextView tv6 = new TextView(this);
                    tv6.setText(String.valueOf(discountamout));
                    tv6.setBackgroundResource(R.drawable.row_border);
                    tv6.setPadding(3,3,3,3);
                    tv6.setGravity(Gravity.CENTER);
                    tv6.setTextColor(Color.parseColor("#303F9F"));
                    row.addView(tv6);

                    TextView tv7 = new TextView(this);
                    tot+=pri*qty-discountamout;
                    tv7.setText(String.valueOf(pri*qty-discountamout));
                    tv7.setBackgroundResource(R.drawable.row_border);
                    tv7.setPadding(3,3,3,3);
                    tv7.setGravity(Gravity.CENTER);
                    tv7.setTextColor(Color.parseColor("#303F9F"));
                    row.addView(tv7);

                    bill_table.addView(row);
                    c1.moveToNext();
                }

            }
            db1.close();

            totallayout = findViewById(R.id.totaltable);
            TableRow rowi = (TableRow) totallayout.getChildAt(0);
            TextView total = (TextView)rowi.getChildAt(1);
            total.setText(String.valueOf(taxtot));

            rowi = (TableRow) totallayout.getChildAt(1);
            total = (TextView)rowi.getChildAt(1);
            total.setText(String.valueOf(taxtot));


            rowi = (TableRow) totallayout.getChildAt(2);
            total = (TextView)rowi.getChildAt(1);
            total.setText(String.valueOf(distot));


            rowi = (TableRow) totallayout.getChildAt(3);
            total = (TextView)rowi.getChildAt(1);
            total.setText(String.valueOf(tot));
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                makepdf();
            }
        });

        th.start();

        SQLiteDatabase db6 = helper5.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(transactions.transaction_number,transactno);
        contentValues.put(transactions.transaction_amount,tot);
        db6.insert(transactions.Tablename,null,contentValues);
        db6.close();


    }

    //sharing the pdf which has been already created and saved with name invoice.pdf
    private void shareit() {

        String root = Environment.getExternalStorageDirectory().toString();
        File mydir = new File(root,"RetailCare");

        if(mydir.exists()){
            String fname = "invoice.pdf";
            File file = new File(mydir,fname);
            if(file.exists()){
                Intent intent=new Intent(Intent.ACTION_SEND);
                Uri uri = Uri.fromFile(file);
                intent.setDataAndType(uri, "application/pdf");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                //intent.setPackage("com.whatsapp");
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // making up the pdf whene the activity is called using itext library
    private void makepdf() {

        if(isExternalStorageWritable()){
            String root = Environment.getExternalStorageDirectory().toString();
            File mydir = new File(root,"RetailCare");

            if(!mydir.exists()){
                if(mydir.mkdirs()) {
                    String fname = "invoice.pdf";
                    File file = new File(mydir,fname);
                    if(file.exists()){
                        file.delete();
                    }

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try{
                        FileOutputStream out = new FileOutputStream(file);
                        Document doc = new Document(PageSize.A8,5,5,5,5);
                        PdfWriter.getInstance(doc,out);
                        doc.open();


                        FontSelector selector = new FontSelector();
                        Font f1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
                        f1.setColor(BaseColor.BLACK);
                        selector.addFont(f1);
                        Phrase ph = selector.process(shop.getText().toString());
                        Paragraph par = new Paragraph(ph);
                        par.setAlignment(Element.ALIGN_CENTER);
                        doc.add(par);

                        FontSelector selector2 = new FontSelector();
                        Font f3 = FontFactory.getFont(FontFactory.HELVETICA_BOLD,4);
                        f3.setColor(BaseColor.BLACK);
                        selector2.addFont(f3);

                        FontSelector selector1 = new FontSelector();
                        Font f2 = FontFactory.getFont(FontFactory.HELVETICA);
                        f1.setColor(BaseColor.BLACK);
                        selector1.addFont(f2);

                        ph = selector2.process("INVOICE DETAILS ");
                        par = new Paragraph(ph);
                        par.setSpacingBefore(3);
                        par.setAlignment(Element.ALIGN_CENTER);
                        doc.add(par);

                        PdfPTable table = new PdfPTable(2);
                        table.setWidthPercentage(100);

                        table.addCell(getCell("Transaction number :",PdfPCell.ALIGN_LEFT));
                        table.addCell(getCell(transactno,PdfPCell.ALIGN_RIGHT));

                        table.addCell(getCell("Customer Name :",PdfPCell.ALIGN_LEFT));
                        table.addCell(getCell(custname,PdfPCell.ALIGN_RIGHT));
                        table.setSpacingBefore(5f);

                        table.addCell(getCell("Customer Mobile :",PdfPCell.ALIGN_LEFT));
                        table.addCell(getCell(custmob,PdfPCell.ALIGN_RIGHT));

                        table.addCell(getCell("Dealer Name :",PdfPCell.ALIGN_LEFT));
                        table.addCell(getCell(dealn.getText().toString(),PdfPCell.ALIGN_RIGHT));

                        table.addCell(getCell("Dealer Mobile:",PdfPCell.ALIGN_LEFT));
                        table.addCell(getCell(dealm.getText().toString(),PdfPCell.ALIGN_RIGHT));


                        table.addCell(getCell("Dealer GSTID:",PdfPCell.ALIGN_LEFT));
                        table.addCell(getCell(dealg.getText().toString(),PdfPCell.ALIGN_RIGHT));
                        doc.add(table);


                        ph = selector2.process("Billing Product Details:");
                        par = new Paragraph(ph);
                        par.setSpacingBefore(5f);
                        par.setAlignment(Element.ALIGN_LEFT);
                        doc.add(par);

                        PdfPTable table1 = new PdfPTable(8);
                        table1.setSpacingBefore(5f);
                        table1.setWidthPercentage(100);
                        table1.addCell(setCell("S.No"));
                        table1.addCell(setCell("Product Name"));
                        table1.addCell(setCell("Quantity"));
                        table1.addCell(setCell("MRP (per item)"));
                        table1.addCell(setCell("CGST (amount)"));
                        table1.addCell(setCell("SGST (amount)"));
                        table1.addCell(setCell("Discount"));
                        table1.addCell(setCell("Final Price"));


                        for(int i=0;i<bill_table.getChildCount()-1;i++){
                            TableRow rowi = (TableRow) bill_table.getChildAt(i+1);
                            for(int j=0;j<8;j++){
                                TextView tx  = (TextView)rowi.getChildAt(j);
                                table1.addCell(settableCell(tx.getText().toString()));
                            }
                        }
                        doc.add(table1);


                        ph = selector2.process("Total pricing:");
                        par = new Paragraph(ph);
                        par.setSpacingBefore(5f);
                        par.setAlignment(Element.ALIGN_LEFT);
                        doc.add(par);

                        PdfPTable table2 = new PdfPTable(2);
                        table2.setWidthPercentage(80);
                        table2.setSpacingBefore(5f);

                        for(int i=0;i<totallayout.getChildCount();i++){
                            TableRow rowi = (TableRow) totallayout.getChildAt(i);
                            for(int j=0;j<2;j++){
                                TextView tx  = (TextView)rowi.getChildAt(j);
                                if(j==0){
                                    table2.addCell(setCell(tx.getText().toString()));
                                }else {
                                    table2.addCell(settableCell(tx.getText().toString()));
                                }
                            }
                        }
                        doc.add(table2);


                        PdfPTable table3 = new PdfPTable(1);
                        table.setHorizontalAlignment(Element.ALIGN_BASELINE);
                        table3.setWidthPercentage(100);
                        table3.setSpacingBefore(5f);
                        table3.addCell(setlastCell("This is an auto generated invoice. It doesn't require signature of the shop owner"));
                        doc.add(table3);

                        doc.close();
                        out.flush();
                        out.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"not able to make directory",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                String fname = "invoice.pdf";
                File file = new File(mydir,fname);
                if(file.exists()){
                    file.delete();
                }

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    Document doc = new Document(PageSize.A8,5,5,5,5);
                    PdfWriter.getInstance(doc,out);
                    doc.open();


                    FontSelector selector = new FontSelector();
                    Font f1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
                    f1.setColor(BaseColor.BLACK);
                    selector.addFont(f1);
                    Phrase ph = selector.process(shop.getText().toString());
                    Paragraph par = new Paragraph(ph);
                    par.setAlignment(Element.ALIGN_CENTER);
                    doc.add(par);

                    FontSelector selector2 = new FontSelector();
                    Font f3 = FontFactory.getFont(FontFactory.HELVETICA_BOLD,4);
                    f3.setColor(BaseColor.BLACK);
                    selector2.addFont(f3);

                    FontSelector selector1 = new FontSelector();
                    Font f2 = FontFactory.getFont(FontFactory.HELVETICA);
                    f1.setColor(BaseColor.BLACK);
                    selector1.addFont(f2);

                    ph = selector2.process("INVOICE DETAILS ");
                    par = new Paragraph(ph);
                    par.setSpacingBefore(3);
                    par.setAlignment(Element.ALIGN_CENTER);
                    doc.add(par);

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);

                    table.addCell(getCell("Transaction number :",PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(transactno,PdfPCell.ALIGN_RIGHT));

                    table.addCell(getCell("Customer Name :",PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(custname,PdfPCell.ALIGN_RIGHT));
                    table.setSpacingBefore(5f);

                    table.addCell(getCell("Customer Mobile :",PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(custmob,PdfPCell.ALIGN_RIGHT));

                    table.addCell(getCell("Dealer Name :",PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(dealn.getText().toString(),PdfPCell.ALIGN_RIGHT));

                    table.addCell(getCell("Dealer Mobile:",PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(dealm.getText().toString(),PdfPCell.ALIGN_RIGHT));


                    table.addCell(getCell("Dealer GSTID:",PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(dealg.getText().toString(),PdfPCell.ALIGN_RIGHT));
                    doc.add(table);


                    ph = selector2.process("Billing Product Details:");
                    par = new Paragraph(ph);
                    par.setSpacingBefore(5f);
                    par.setAlignment(Element.ALIGN_LEFT);
                    doc.add(par);

                    PdfPTable table1 = new PdfPTable(8);
                    table1.setSpacingBefore(5f);
                    table1.setWidthPercentage(100);
                    table1.addCell(setCell("S.No"));
                    table1.addCell(setCell("Product Name"));
                    table1.addCell(setCell("Quantity"));
                    table1.addCell(setCell("MRP (per item)"));
                    table1.addCell(setCell("CGST (amount)"));
                    table1.addCell(setCell("SGST (amount)"));
                    table1.addCell(setCell("Discount"));
                    table1.addCell(setCell("Final Price"));


                    for(int i=0;i<bill_table.getChildCount()-1;i++){
                        TableRow rowi = (TableRow) bill_table.getChildAt(i+1);
                        for(int j=0;j<8;j++){
                            TextView tx  = (TextView)rowi.getChildAt(j);
                            table1.addCell(settableCell(tx.getText().toString()));
                        }
                    }
                    doc.add(table1);


                    ph = selector2.process("Total pricing:");
                    par = new Paragraph(ph);
                    par.setSpacingBefore(5f);
                    par.setAlignment(Element.ALIGN_LEFT);
                    doc.add(par);

                    PdfPTable table2 = new PdfPTable(2);
                    table2.setWidthPercentage(80);
                    table2.setSpacingBefore(5f);

                    for(int i=0;i<totallayout.getChildCount();i++){
                        TableRow rowi = (TableRow) totallayout.getChildAt(i);
                        for(int j=0;j<2;j++){
                            TextView tx  = (TextView)rowi.getChildAt(j);
                            if(j==0){
                                table2.addCell(setCell(tx.getText().toString()));
                            }else {
                                table2.addCell(settableCell(tx.getText().toString()));
                            }
                        }
                    }
                    doc.add(table2);


                    PdfPTable table3 = new PdfPTable(1);
                    table.setHorizontalAlignment(Element.ALIGN_BASELINE);
                    table3.setWidthPercentage(100);
                    table3.setSpacingBefore(5f);
                    table3.addCell(setlastCell("This is an auto generated invoice. It doesn't require signature of the shop owner"));
                    doc.add(table3);

                    doc.close();
                    out.flush();
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public PdfPCell getCell(String text, int alignment) {

        FontSelector selector = new FontSelector();
        Font f1 = FontFactory.getFont(FontFactory.HELVETICA, 3);
        f1.setColor(BaseColor.BLACK);
        selector.addFont(f1);
        Phrase ph = new Phrase();
        ph=selector.process(text);
        PdfPCell cell = new PdfPCell(ph);
        cell.setPadding(1);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }


    public PdfPCell setCell(String text) {
        FontSelector selector2 = new FontSelector();
        Font f3 = FontFactory.getFont(FontFactory.HELVETICA_BOLD,3);
        f3.setColor(BaseColor.BLACK);
        selector2.addFont(f3);
        Phrase ph = selector2.process(text);
        PdfPCell cell = new PdfPCell(ph);
        cell.setPadding(2);
        cell.setColspan(1);
        cell.setBorder(Rectangle.BOTTOM| Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
        return cell;
    }

    public PdfPCell settableCell(String text) {

        FontSelector selector = new FontSelector();
        Font f1 = FontFactory.getFont(FontFactory.HELVETICA, 2);
        f1.setColor(BaseColor.BLACK);
        selector.addFont(f1);
        Phrase ph = new Phrase();
        ph=selector.process(text);
        PdfPCell cell = new PdfPCell(ph);
        cell.setPadding(2);
        cell.setColspan(1);
        cell.setBorder(Rectangle.BOTTOM| Rectangle.TOP | Rectangle.RIGHT | Rectangle.LEFT);
        return cell;
    }

    public PdfPCell setlastCell(String text) {
        FontSelector selector = new FontSelector();
        Font f1 = FontFactory.getFont(FontFactory.HELVETICA, 2);
        f1.setColor(BaseColor.BLACK);
        selector.addFont(f1);
        Phrase ph = new Phrase();
        ph=selector.process(text);
        PdfPCell cell = new PdfPCell(ph);
        cell.setPadding(2);
        cell.setColspan(1);
        cell.setBorder(1);
        return cell;
    }



    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
