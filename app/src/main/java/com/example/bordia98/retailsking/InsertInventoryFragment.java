package com.example.bordia98.retailsking;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class InsertInventoryFragment extends Fragment {

    insertclicklistner activitycommander;


    String numbers;
    public interface  insertclicklistner{
        public void scanbarcode(View v);
        public void savetheproduct(String proname, String numbers, Float pr, int qty,Float gstpercent,Float discount);
    }

    Button barcodescanner,saveproduct;
    EditText productname,quantity,price,barcodenumber,discount_percent;
    Spinner gstspinner;
    public InsertInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        numbers=InventoryMainPage.barnumber;
        barcodenumber.setText(numbers);
        onResume();
        InventoryMainPage.barnumber="";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InventoryMainPage.setTitle("Insert Product");
        View view =inflater.inflate(R.layout.fragment_insert_inventory, container, false);
        barcodescanner = view.findViewById(R.id.barcodeScanner);
        barcodenumber = (EditText)view.findViewById(R.id.barcode_number);
        productname = (EditText)view.findViewById(R.id.product_name);
        quantity = (EditText)view.findViewById(R.id.quantity);
        price = (EditText)view.findViewById(R.id.price);
        discount_percent =(EditText)view.findViewById(R.id.discount);

        gstspinner = (Spinner)view.findViewById(R.id.GST_info);

        saveproduct = view.findViewById(R.id.save_and_proceed);
        onStart();
        barcodescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodenumber.setText("");
                productname.setText("");
                price.setText("");
                discount_percent.setText("0");
                quantity.setText("1");
                saveproduct.setEnabled(false);
                Scancode(v);
            }
        });

        saveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty=1;
                try {
                    if (quantity.getText().toString().length() != 0) {
                        qty = Integer.valueOf(quantity.getText().toString().trim());
                        if (qty <= 0) {
                            quantity.setError("Quantity of product can't be less than zero");
                            quantity.requestFocus();
                            return;
                        }
                    } else {
                        quantity.setError("Quantity of product can't be null");
                        quantity.requestFocus();
                        return;
                    }
                    Float pr;
                    if (price.getText().toString().length() != 0) {
                        pr = Float.valueOf(price.getText().toString().trim());
                        if (pr <= 0) {
                            price.setError("Price can't be negative");
                            price.requestFocus();
                            return;
                        }
                    } else {
                        price.setError("Price can't be null");
                        price.requestFocus();
                        return;
                    }
                    String proname;
                    proname = productname.getText().toString();
                    if (proname.length() == 0) {
                        productname.setError("This field can't be empty");
                        productname.requestFocus();
                        return;
                    }

                    Float gstpercent;
                    Float discount;
                    if (discount_percent.getText().toString().length() != 0) {
                        discount = Float.valueOf(discount_percent.getText().toString());

                        if (discount < 0) {
                            discount_percent.setError("Not possible gst percent");
                            discount_percent.requestFocus();
                            return;
                        }
                    } else {
                        discount_percent.setError("Please enter the value");
                        discount_percent.requestFocus();
                        return;
                    }


                    numbers = barcodenumber.getText().toString().trim();
                    String text = gstspinner.getSelectedItem().toString();

                    if (text.equals("GST_0")) {
                        gstpercent = Float.valueOf(0);
                    } else if (text.equals("GST_1")) {
                        gstpercent = Float.valueOf(1);
                    } else if (text.equals("GST_5")) {
                        gstpercent = Float.valueOf(5);
                    } else if (text.equals("GST_12")) {
                        gstpercent = Float.valueOf(12);
                    } else if (text.equals("GST_18")) {
                        gstpercent = Float.valueOf(18);
                    } else {
                        gstpercent = Float.valueOf(28);
                    }

                    Log.v("GST_percent", gstpercent + "");
                    saveproductindatabase(v, proname, numbers, pr, qty, gstpercent, discount);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Please check the input format",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void saveproductindatabase(View v,String proname, String numbers, Float pr, int qty,Float gstpercent,Float discount) {
        activitycommander.savetheproduct(proname,numbers,pr,qty,gstpercent,discount);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(numbers.length()!=0){
            saveproduct.setEnabled(true);
            try {

                StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getContext());
                SQLiteDatabase db = helper.getReadableDatabase();
                String query = "Select * from retail_Store_Details where Product_Barcode=\"" + numbers+"\"";
                Cursor c = db.rawQuery(query,null);
                if(c!=null && c.getCount()>0){
                    c.moveToFirst();
                    while(!c.isAfterLast()){
                        String pro = c.getString(1);
                        Float gstper = c.getFloat(3);
                        Float pri = c.getFloat(4);
                        Float dis = c.getFloat(5);
                        productname.setText(pro);
                        price.setText(String.valueOf(pri));
                        discount_percent.setText(String.valueOf(dis));

                        if(gstper==0){
                            gstspinner.setSelection(0);
                        }else if(gstper==1){
                            gstspinner.setSelection(1);
                        }else if(gstper==5){
                            gstspinner.setSelection(2);
                        }else if(gstper==12){
                            gstspinner.setSelection(3);
                        }else if(gstper==18){
                            gstspinner.setSelection(4);
                        }else{
                            gstspinner.setSelection(5);
                        }

                        c.moveToNext();
                    }

                }
                db.close();
            }catch (Exception e){
                Toast.makeText(getContext(),"Some Error had occured",Toast.LENGTH_SHORT).show();
            }
        }
        InventoryMainPage.barnumber="";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activitycommander=(insertclicklistner) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public void Scancode(View v) {
        activitycommander.scanbarcode(v);
    }
}