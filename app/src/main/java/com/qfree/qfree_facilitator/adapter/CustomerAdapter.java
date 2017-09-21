package com.qfree.qfree_facilitator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Customer;

import java.util.List;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    List<Customer> customers;
    private int rowLayout;
    private Context context;

    public CustomerAdapter(List<Customer> customers, int rowLayout, Context context) {
        this.customers = customers;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        holder.customerName.setText(customers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout customerLayout;
        TextView customerName;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            this.customerLayout = (LinearLayout) itemView.findViewById(R.id.layout_li_customers);
            this.customerName = (TextView) itemView.findViewById(R.id.tv_li_customer_name);
        }
    }


}
