package com.qfree.qfree_facilitator.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Fahad Qureshi on 9/12/2017.
 */

public class Queue implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("isRunning")
    private Boolean isRunning;
    @SerializedName("rear")
    private Long rear;
    @SerializedName("front")
    private Long front;
    @SerializedName("facility")
    private String facilityId;

    public Queue() {
    }

    public Queue(String id, String name, Boolean isRunning, Long rear, Long front, String facilityId) {
        this.id = id;
        this.name = name;
        this.isRunning = isRunning;
        this.rear = rear;
        this.front = front;
        this.facilityId = facilityId;
    }

    public Queue(String name) {
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

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public Long getRear() {
        return rear;
    }

    public void setRear(Long rear) {
        this.rear = rear;
    }

    public Long getFront() {
        return front;
    }

    public void setFront(Long front) {
        this.front = front;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }
}
