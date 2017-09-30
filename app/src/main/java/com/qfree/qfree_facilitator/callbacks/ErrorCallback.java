package com.qfree.qfree_facilitator.callbacks;

import retrofit2.Response;

/**
 * Created by Fahad Qureshi on 9/24/2017.
 */

public interface ErrorCallback {
    void onError(Response response);
    void onError(String errStr);
}
