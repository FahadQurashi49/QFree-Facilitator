package com.qfree.qfree_facilitator.rest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Response;

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

    public static void ShowError(String TAG, Response response, Context context) {
        try {
            RestError errorResponse = ApiClient.getErrorResponse(response.errorBody());
            Log.e(TAG, errorResponse.getErrMsg());
            Toast.makeText(context, errorResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.getMessage());
        }
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
