package com.example.bordia98.retailsking;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class HomeInventoryFragment extends Fragment {

    RecyclerView recyclerView;
    productadapter adapter;
    List<product> productList;
    public HomeInventoryFragment() {
        // Required empty public constructor
    }

    String[] productnames;
    String[] prices;
    String[] quantities;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InventoryMainPage.setTitle("Inventory");

        productList = new ArrayList<>();
        View view;
        view =inflater.inflate(R.layout.fragment_home_inventory, container, false);
        recyclerView = view.findViewById(R.id.productlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        StoreManagerDB_Helper helper = new StoreManagerDB_Helper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "Select Product_Name,Product_Price,Product_Quantity from retail_Store_Details";
        Cursor c = db.rawQuery(query,null);
        int arraylen = c.getCount();

        if(c!=null && c.getCount()>0){
            productnames = new String[arraylen];
            prices = new String[arraylen];
            quantities = new String[arraylen];
            c.moveToFirst();
            int k=0;
            while(!c.isAfterLast()){
                productnames[k]=(k+1)+")   "+ c.getString(0);
                prices[k]= String.valueOf(c.getFloat(1));
                Log.v("Prices of the items",prices[k]);
                quantities[k] = String.valueOf(c.getInt(2));
                k++;
                c.moveToNext();
            }

            int i;
            for(i=0;i<productnames.length;i++){
                product obj = new product(productnames[i],Integer.valueOf(quantities[i]),Float.valueOf(prices[i]));
                productList.add(obj);
            }
        }
        db.close();


//        ListView ls = view.findViewById(R.id.productlist);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.listview, R.id.textView,productnames);
//        ls.setAdapter(arrayAdapter);

        adapter = new productadapter(getContext(),productList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
