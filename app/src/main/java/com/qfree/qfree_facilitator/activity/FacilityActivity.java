package com.qfree.qfree_facilitator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Facility;
import com.qfree.qfree_facilitator.service.FacilityService;

public class FacilityActivity extends AppCompatActivity {
    private Facility facility;

    private TextView facilityNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        facilityNameTextView = (TextView) findViewById(R.id.tv_facility_name);

        facility = FacilityService.getInstance().getFacilityInstance();

        facilityNameTextView.setText(facility.getName());

    }
}
