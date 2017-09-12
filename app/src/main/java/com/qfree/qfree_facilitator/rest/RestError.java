package com.qfree.qfree_facilitator.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fahad Qureshi on 9/9/2017.
 */

public class RestError {
    @SerializedName("error")
    private String errMsg;
    @SerializedName("errorCode")
    private int errCode;
    @SerializedName("statusCode")
    private int statusCode;

    public RestError() {
    }

    public RestError(String errMsg, int errCode, int statusCode) {
        this.errMsg = errMsg;
        this.errCode = errCode;
        this.statusCode = statusCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
