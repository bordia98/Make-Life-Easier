package com.example.bordia98.retailsking;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class EditInventoryFragment extends Fragment {


    editclicklistner activitycommander;


    String numbers;
    public interface  editclicklistner{
        public void scaneditbarcode(View v);
        public void updateproductdetails(String proname, String numbers, Float pr, int qty,Float gstpercent,Float discount);
    }

    Button editbarcodescanner,saveproduct;
    EditText productname,quantity,price,barcodenumber,discount_percent;
    Spinner gstspinner;


    @Override
    public void onStart() {
        super.onStart();
        numbers=InventoryMainPage.barnumber;
        onResume();
        InventoryMainPage.barnumber="";
    }

    public EditInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InventoryMainPage.setTitle("Edit Product Details");
        View view =inflater.inflate(R.layout.fragment_edit_inventory, container, false);
        editbarcodescanner = view.findViewById(R.id.editbarcodeScanner);
        barcodenumber = (EditText)view.findViewById(R.id.editbarcode_number);
        productname = (EditText)view.findViewById(R.id.editproduct_name);
        quantity = (EditText)view.findViewById(R.id.editquantity);
        price = (EditText)view.findViewById(R.id.editprice);
        gstspinner = (Spinner) view.findViewById(R.id.GST_info);
        discount_percent =(EditText)view.findViewById(R.id.discount);

        saveproduct = view.findViewById(R.id.update_and_proceed);
        editbarcodescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodenumber.setText("");
                productname.setText("");
                price.setText("");
                quantity.setText("");
                discount_percent.setText("");
                saveproduct.setEnabled(false);
                Scancode(v);
            }
        });

        saveproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int qty=0;
                    if(quantity.getText().toString().length()!=0){
                        qty = Integer.valueOf(quantity.getText().toString().trim());
                        if(qty<=0){
                            quantity.setError("Quantity of product can't be less than zero");
                            quantity.requestFocus();
                            return;
                        }
                    }else{
                        quantity.setError("Quantity of product can't be null");
                        quantity.requestFocus();
                        return;
                    }
                    Float pr;
                    if(price.getText().toString().length()!=0) {
                        pr = Float.valueOf(price.getText().toString().trim());
                        if (pr <= 0) {
                            price.setError("Price can't be negative");
                            price.requestFocus();
                            return;
                        }
                    }else{
                        price.setError("Price can't be null");
                        price.requestFocus();
                        return;
                    }
                    String proname;
                    proname = productname.getText().toString();
                    if(proname.length()==0){
                        productname.setError("This field can't be empty");
                        productname.requestFocus();
                        return;
                    }

                    Float gstpercent;
//                if(gst_percent.getText().toString().length()!=0){
//                    gstpercent = Float.valueOf(gst_percent.getText().toString());
//
//                    if(gstpercent<0){
//                        gst_percent.setError("Not possible gst percent");
//                        gst_percent.requestFocus();
//                        return;
//                    }
//                }else{
//                    gst_percent.setError("Please enter the value");
//                    gst_percent.requestFocus();
//                    return;
//                }

                    Float discount;
                    if(discount_percent.getText().toString().length()!=0){
                        discount = Float.valueOf(discount_percent.getText().toString());

                        if(discount<0){
                            discount_percent.setError("Not possible gst percent");
                            discount_percent.requestFocus();
                            return;
                        }
                    }else{
                        discount_percent.setError("Please enter the value");
                        discount_percent.requestFocus();
                        return;
                    }

                    String text = gstspinner.getSelectedItem().toString();

                    if(text.equals("GST_0")){
                        gstpercent=Float.valueOf(0);
                    }else if(text.equals("GST_1")){
                        gstpercent=Float.valueOf(1);
                    }else if(text.equals("GST_5")){
                        gstpercent=Float.valueOf(5);
                    }else if(text.equals("GST_12")){
                        gstpercent=Float.valueOf(12);
                    }else if(text.equals("GST_18")){
                        gstpercent=Float.valueOf(18);
                    }else{
                        gstpercent=Float.valueOf(28);
                    }

                    numbers=barcodenumber.getText().toString().trim();
                    updateproductdetailsindatabase(v,proname,numbers,pr,qty,gstpercent,discount);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Some error had occured",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void updateproductdetailsindatabase(View v, String proname, String numbers, Float pr, int qty,Float gstper,Float discount) {
        activitycommander.updateproductdetails(proname,numbers,pr,qty,gstper,discount);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activitycommander=(EditInventoryFragment.editclicklistner) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        numbers=InventoryMainPage.barnumber;
        if(numbers.length()!=0){
            StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "Select * from retail_Store_Details where Product_Barcode=\"" + numbers+"\"";
            Cursor c = db.rawQuery(query,null);
            if(c!=null && c.getCount()>0){
                barcodenumber.setText(numbers);
                saveproduct.setEnabled(true);
                c.moveToFirst();
                while(!c.isAfterLast()){
                    String pro = c.getString(1);
                    int qty = c.getInt(2);
                    Float gstper = c.getFloat(3);
                    Float pri = c.getFloat(4);
                    Float dis = c.getFloat(5);
                    productname.setText(pro);
                    price.setText(String.valueOf(pri));
                    quantity.setText(String.valueOf(qty));
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
            else{
                Toast.makeText(getContext(),"No such product is present in database",Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
        InventoryMainPage.barnumber="";
    }

    public void Scancode(View v) {
        activitycommander.scaneditbarcode(v);
    }

}