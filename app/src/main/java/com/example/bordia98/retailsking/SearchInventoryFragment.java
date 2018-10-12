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

public class SearchInventoryFragment extends Fragment {

    searchclicklistner activitycommandar;

    // interface for implementing the methods

    public interface searchclicklistner{
        public void scancodeforsearch(View v);

    }

    String numbers;
    Button searchbarcodescanner;
    TextView productname,quantity,price;

    public SearchInventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activitycommandar=(SearchInventoryFragment.searchclicklistner) activity;
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
        InventoryMainPage.setTitle("Search Product");
        View view=  inflater.inflate(R.layout.fragment_search_inventory, container, false);
        searchbarcodescanner = view.findViewById(R.id.barcodeScanner);
        productname = view.findViewById(R.id.searchproduct_title);
        quantity=view.findViewById(R.id.searchproduct_quantity);
        price= view.findViewById(R.id.searchproduct_price);

        //scanning the barcode when the activity is called
        searchbarcodescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productname.setText("Productname");
                price.setText("");
                quantity.setText("");
                scancode(v);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // if scanned successfully then barcode is returned
        numbers=InventoryMainPage.barnumber;

        if(numbers.length()!=0){
            StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();
            String query = "Select * from retail_Store_Details where Product_Barcode=\"" + numbers+"\"";
            Cursor c = db.rawQuery(query,null);
            //checking product is there in database or not
            if(c!=null && c.getCount()>0){
                c.moveToFirst();
                while(!c.isAfterLast()){
                    String pro = c.getString(1);
                    int qty = c.getInt(2);
                    Float pri = c.getFloat(4);
                    productname.setText(pro);
                    price.setText(String.valueOf(pri));
                    quantity.setText(String.valueOf(qty));
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
        activitycommandar.scancodeforsearch(v);
    }

}
