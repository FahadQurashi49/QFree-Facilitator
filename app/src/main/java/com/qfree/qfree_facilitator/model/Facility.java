package com.qfree.qfree_facilitator.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fahad Qureshi on 9/8/2017.
 */

public class Facility {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;

    public Facility() {
    }

    public Facility(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
