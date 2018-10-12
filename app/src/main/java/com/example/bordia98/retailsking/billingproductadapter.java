package com.example.bordia98.retailsking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class billingproductadapter extends RecyclerView.Adapter<billingproductadapter.productviewholder>{

    public Context context;
    public List<billing_product> productList;

    public billingproductadapter(Context context, List<billing_product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public productviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.billing_product,null);
        productviewholder holder = new productviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(productviewholder holder, int position) {

        billing_product product = productList.get(position);
        holder.producttitle.setText(product.getProduct_title());
        holder.productqty.setText(String.valueOf(product.getProduct_qty()));
        holder.productprice.setText(String.valueOf(product.getProduct_price()));
        holder.productbarcode.setText(product.getProduct_barcode());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class productviewholder extends RecyclerView.ViewHolder{

        TextView producttitle,productprice,productbarcode,productqty;

        public productviewholder(View itemView) {
            super(itemView);
            producttitle = itemView.findViewById(R.id.product_title);
            productprice = itemView.findViewById(R.id.product_price);
            productbarcode = itemView.findViewById(R.id.palcetoinsertbarcode);
            productqty = itemView.findViewById(R.id.product_quantity);
        }
    }
}
