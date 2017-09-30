package com.qfree.qfree_facilitator.callbacks;

import com.qfree.qfree_facilitator.model.Customer;

/**
 * Created by Fahad Qureshi on 9/23/2017.
 */

public interface CustomerCallback extends ErrorCallback {

    void onCustomerReceive(Customer customer);
}
