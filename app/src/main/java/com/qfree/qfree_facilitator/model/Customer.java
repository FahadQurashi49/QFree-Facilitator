package com.qfree.qfree_facilitator.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public class Customer {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("queueNumber")
    private Long queueNumber;
    @SerializedName("isInQueue")
    private Boolean isInQueue;
    @SerializedName("queue")
    private String queueId;

    public Customer() {
    }

    public Customer(String id, String name, Long queueNumber, Boolean isInQueue, String queueId) {
        this.id = id;
        this.name = name;
        this.queueNumber = queueNumber;
        this.isInQueue = isInQueue;
        this.queueId = queueId;
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

    public Long getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(Long queueNumber) {
        this.queueNumber = queueNumber;
    }

    public Boolean getInQueue() {
        return isInQueue;
    }

    public void setInQueue(Boolean inQueue) {
        isInQueue = inQueue;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }
}
