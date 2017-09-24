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
    private String errCode;
    @SerializedName("statusCode")
    private int statusCode;

    public RestError() {
    }

    public RestError(String errMsg, String errCode, int statusCode) {
        this.errMsg = errMsg;
        this.errCode = errCode;
        this.statusCode = statusCode;
    }

    public static boolean ShowIfError(String TAG, Response response, Context context) {
        try {
            if (IsResponseError(response)) {
                RestError errorResponse = ApiClient.getErrorResponse(response.errorBody());
                Log.e(TAG, errorResponse.getErrMsg());
                Toast.makeText(context, errorResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            ShowError(TAG, e.getMessage(), context);
            return false;
        }
        return true;
    }
    public static boolean IsResponseError (Response response) {
        return response != null && !response.isSuccessful() && response.errorBody() != null;
    }
    public static void ShowError(String TAG, String errMsg, Context context) {
        Log.e(TAG, errMsg);
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
    }


    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
