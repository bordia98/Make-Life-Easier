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
import android.widget.TextView;


public class DeleteInventoryFragment extends Fragment {


    deleteclicklistner activitycommandar;

    public interface deleteclicklistner{
        public void scancodefordelete(View v);
        public void deleterecord(String barcodenumber);

    }

    String numbers;
    Button deletebarcodescanner,deletebutton;
    TextView productname,quantity,price,barcodenumber;

    public DeleteInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activitycommandar=(DeleteInventoryFragment.deleteclicklistner) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InventoryMainPage.setTitle("Delete Product");
        View view = inflater.inflate(R.layout.fragment_delete_inventory, container, false);
        deletebarcodescanner = view.findViewById(R.id.barcodeScanner);
        productname = view.findViewById(R.id.deleteproduct_title);
        quantity=view.findViewById(R.id.deleteproduct_quantity);
        price= view.findViewById(R.id.deleteproduct_price);
        barcodenumber = view.findViewById(R.id.palcetoinsertbarcode);
        deletebutton = view.findViewById(R.id.deleterecord);

        deletebarcodescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productname.setText("Productname");
                price.setText("");
                quantity.setText("");
                barcodenumber.setText("");
                deletebutton.setEnabled(false);
                scancode(v);
            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barnumber = barcodenumber.getText().toString().trim();
                deleterecord(v,barnumber);
            }
        });
        return view;

    }

    public void deleterecord(View v,String barcodenumber) {
        activitycommandar.deleterecord(barcodenumber);
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
                c.moveToFirst();
                while(!c.isAfterLast()){
                    String pro = c.getString(1);
                    int qty = c.getInt(2);
                    Float pri = c.getFloat(4);
                    productname.setText(pro);
                    price.setText(String.valueOf(pri));
                    quantity.setText(String.valueOf(qty));
                    barcodenumber.setText(numbers);
                    deletebutton.setEnabled(true);
                    c.moveToNext();
                }
            }else{
                productname.setText("No match found");
                price.setText("--");
                quantity.setText("--");
            }
            db.close();
        }
        InventoryMainPage.barnumber="";
    }



    private void scancode(View v) {
        activitycommandar.scancodefordelete(v);
    }

}
