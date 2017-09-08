package com.qfree.qfree_facilitator.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fahad Qureshi on 9/8/2017.
 */

public class ApiClient {
    public static final String BASE_URL = "http://192.168.1.6:4000/api/"; // set baseUrl here
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
