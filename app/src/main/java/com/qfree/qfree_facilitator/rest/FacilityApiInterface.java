package com.qfree.qfree_facilitator.rest;

import com.qfree.qfree_facilitator.model.Facility;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Fahad Qureshi on 9/8/2017.
 */

public interface FacilityApiInterface {
    @GET("facilities/{id}")
    Call<Facility> getFacility(@Path("id") String id);
}
