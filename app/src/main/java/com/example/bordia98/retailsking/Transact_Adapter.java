package com.example.bordia98.retailsking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;


// Adapter for Transact_History class

public class Transact_Adapter extends RecyclerView.Adapter<Transact_Adapter.productviewholder>{
    public Context context;
    public List<transact_history> history;

    // Constructor for the adapter
    public Transact_Adapter(Context context, List<transact_history> History) {
        this.context = context;
        this.history = History;
    }

    @Override
    public productviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction,null);  // layouting transaction view carring the cardview
        productviewholder holder = new productviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(productviewholder holder, final int position) {

        transact_history histo = history.get(position);

        // Getting the element from the list and setting the details

        holder.amount.setText(histo.getAmount());
        holder.transactionno.setText(histo.getTransactionnumber());

        // setting up on click listner to start the delete activity

        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Delete_transaction.class);
                i.putExtra("tno",history.get(position).transactionnumber);
                i.putExtra("amt",history.get(position).amount);
                context.startActivity(i);
            }
        });

    }

    // returns the number of items in recyclerview
    @Override
    public int getItemCount() {
        return history.size();
    }

    class productviewholder extends RecyclerView.ViewHolder{

        // instantiating the fields in the cardview of transaction layout
        TextView amount,transactionno;
        LinearLayout parentlayout;

        public productviewholder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            transactionno = itemView.findViewById(R.id.transaction_number);
            parentlayout = itemView.findViewById(R.id.parentlayout);
        }

    }
}

