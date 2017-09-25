package com.qfree.qfree_facilitator.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Customer;
import com.qfree.qfree_facilitator.model.Facility;
import com.qfree.qfree_facilitator.model.Queue;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.QueueApiInterface;
import com.qfree.qfree_facilitator.rest.RestError;
import com.qfree.qfree_facilitator.service.FacilityService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQueueActivity extends AppCompatActivity {
    private static final String TAG = FacilityActivity.class.getSimpleName();

    private EditText queueNameEditText;

    private QueueApiInterface queueApiService;
    private Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);

        facility = FacilityService.getInstance().getFacilityInstance();
        queueApiService = ApiClient.getClient().create(QueueApiInterface.class);

        queueNameEditText = (EditText) findViewById(R.id.et_queue_name);
    }

    public void onBtnAddQueueClick(View view){
        String queueName = queueNameEditText.getText().toString().trim();
        if(validateQueueForm(queueName)) {
            Queue queue = new Queue(queueName);
            Call<Queue> addQueueCall = queueApiService.addQueue(facility.getId(), queue);
            addQueueCall.enqueue(new Callback<Queue>() {
                @Override
                public void onResponse(Call<Queue> call, Response<Queue> response) {
                    try {
                        if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                            Queue addedQueue = response.body();
                            Toast.makeText(getApplicationContext(), "Successfully added " + addedQueue.getName(), Toast.LENGTH_SHORT).show();
                            finishActivity(addedQueue);
                        }
                    } catch (Exception e) {
                        RestError.ShowError(TAG, e.getMessage(), getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<Queue> call, Throwable t) {
                    RestError.ShowError(TAG, t.getMessage(), getApplicationContext());
                }
            });

        }



    }
    private boolean validateQueueForm (String queueName) {
        String errMsg = "";
        if (queueName.isEmpty()) {
            errMsg = "Queue name is required";
        } else if (queueName.length() < 2) {
            errMsg = "Queue name must be of atleast two characters";
        }
        if (!errMsg.isEmpty()) {
            RestError.ShowError(TAG, errMsg, getApplicationContext());
            return  false;
        }
        return true;
    }

    private void finishActivity(Queue queue) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("addedQueue", queue);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
