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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    List<Customer> customers;
    Map<String, Customer> customerMap;
    private int rowLayout;
    private Context context;

    public CustomerAdapter(int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.context = context;
        this.customers = new ArrayList<>();
        this.customerMap = new HashMap<>();
    }

    public CustomerAdapter(List<Customer> customers, int rowLayout, Context context) {
        this.setCustomers(customers);
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

    private void createCustomerMap(List<Customer> customers) {
        this.customerMap = new HashMap<>();
        for (Customer customer: customers) {
            this.customerMap.put(customer.getId(), customer);
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        createCustomerMap(customers);
        notifyDataSetChanged();
    }

    public void clearList() {
        if (this.customers.size() > 0) {
            this.customers.clear();
            notifyDataSetChanged();
        }
    }
    public void removeCustomer(Customer customer) {
        if (this.customers != null &&
                this.customers.size() > 0 &&
                this.customerMap != null) {
            Customer customerInList = this.customerMap.get(customer.getId());
            if (customerInList != null) {
                int index = this.customers.indexOf(customerInList);
                if (this.customers.remove(customerInList)) {
                    notifyItemRemoved(index);
                }
            }

        }
    }

    public void addCustomer(Customer customer) {
        if (customer != null && this.customers != null &&
                this.customerMap != null) {
            // add customer at the last of the list
            int index = this.customers.size();
            this.customers.add(index, customer);
            this.customerMap.put(customer.getId(), customer);
            notifyItemInserted(index);

        }
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
