package com.example.bordia98.retailsking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

//Adapter class for the billing product
public class productadapter extends RecyclerView.Adapter<productadapter.productviewholder> {

    private Context context;
    private List<product> productList;

    //constructor
    public productadapter(Context context, List<product> productList) {
        this.context = context;
        this.productList = productList;
    }

    //view holder
    @Override
    public productviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ddisplay_layout,null);
        productviewholder holder = new productviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(productviewholder holder, int position) {

        //assing the values to the fields and fixing them in layout
        product product = productList.get(position);
        holder.producttitle.setText(product.getProduct_title());
        holder.productquantity.setText(String.valueOf(product.getProduct_qty()));
        holder.productprice.setText(String.valueOf(product.getProduct_price()));

    }

    //returns the item count in recycler  view
    @Override
    public int getItemCount() {
        return productList.size();
    }

    //view holder class
    class productviewholder extends RecyclerView.ViewHolder{

        //holds the element of cardview of the layout
        TextView producttitle,productprice,productquantity;


        public productviewholder(View itemView) {
            super(itemView);
            producttitle = itemView.findViewById(R.id.product_title);
            productprice = itemView.findViewById(R.id.product_price);
            productquantity = itemView.findViewById(R.id.product_quantity);

        }
    }
}
