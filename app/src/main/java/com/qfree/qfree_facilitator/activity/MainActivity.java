package com.qfree.qfree_facilitator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Facility;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.FacilityApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView facilityNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        facilityNameTextView = (TextView) findViewById(R.id.tv_facility_name);

        FacilityApiInterface facilityApiService = ApiClient.getClient()
                .create(FacilityApiInterface.class);

        Call<Facility> call = facilityApiService.getFacility("59aed509ae40df11a81934db");
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                Facility facility = response.body();
                if (facility != null) {
                    facilityNameTextView.setText(facility.getName());
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                facilityNameTextView.setText(t.getMessage());
            }
        });
    }
}
