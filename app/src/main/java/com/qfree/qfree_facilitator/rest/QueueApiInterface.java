package com.qfree.qfree_facilitator.rest;

import com.qfree.qfree_facilitator.model.PageResponse;
import com.qfree.qfree_facilitator.model.Queue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public interface QueueApiInterface {
    @GET("facilities/{id}/queues")
    Call<PageResponse<Queue>> getAllQueues(@Path("id") String facilityId);
}
