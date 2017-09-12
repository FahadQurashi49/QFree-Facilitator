package com.qfree.qfree_facilitator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Facility;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.FacilityApiInterface;
import com.qfree.qfree_facilitator.rest.RestError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    FacilityApiInterface facilityApiService;

    private EditText facilityIdEditText;
    private TextView loginStatusTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        facilityIdEditText = (EditText) findViewById(R.id.et_facility_id);
        loginStatusTextView = (TextView) findViewById(R.id.tv_login_status);

        facilityApiService = ApiClient.getClient()
                .create(FacilityApiInterface.class);
    }

    public void onBtnLoginClick(View view) {
        String facilityId = facilityIdEditText.getText().toString();
        if (facilityId != null && !facilityId.isEmpty()) {
            Call<Facility> call = facilityApiService.getFacility(facilityId);
            call.enqueue(new Callback<Facility>() {
                @Override
                public void onResponse(Call<Facility> call, Response<Facility> response) {
                    if (response != null) {
                        if (response.isSuccessful()) {
                            Facility facility = response.body();
                            if (facility != null) {
                                loginStatusTextView.setText(facility.getName());
                            }
                        } else if (response.errorBody() != null) {

                            try {
                                RestError errorResponse = ApiClient.getErrorResponse(response.errorBody());
                                loginStatusTextView.setText(errorResponse.getErrMsg());
                            } catch (IOException e) {
                                loginStatusTextView.setText(e.getMessage());
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<Facility> call, Throwable t) {
                    loginStatusTextView.setText(t.toString());
                }
            });
        } else {
            //handle error
        }
    }
}
