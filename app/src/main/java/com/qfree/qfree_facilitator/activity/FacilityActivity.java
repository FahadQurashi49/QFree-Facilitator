package com.qfree.qfree_facilitator.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.adapter.QueueAdapter;
import com.qfree.qfree_facilitator.adapter.QueueAdapterListener;
import com.qfree.qfree_facilitator.model.Facility;
import com.qfree.qfree_facilitator.model.PageResponse;
import com.qfree.qfree_facilitator.model.Queue;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.QueueApiInterface;
import com.qfree.qfree_facilitator.rest.RestError;
import com.qfree.qfree_facilitator.service.FacilityService;
import com.qfree.qfree_facilitator.service.QueueService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacilityActivity extends AppCompatActivity {
    private static final String TAG = FacilityActivity.class.getSimpleName();
    private static final int SELECTED_QUEUE_STATE = 1;

    private Facility facility;
    private TextView facilityNameTextView;

    private QueueApiInterface queueApiService;

    private RecyclerView queuesRecyclerView;
    private QueueAdapter queueAdapter;

    private int selectedQueuePosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        queueApiService = ApiClient.getClient().create(QueueApiInterface.class);

        facilityNameTextView = (TextView) findViewById(R.id.tv_facility_name);
        queuesRecyclerView = (RecyclerView) findViewById(R.id.rv_queues);

        facility = FacilityService.getInstance().getFacilityInstance();
         queuesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        facilityNameTextView.setText(facility.getName());

        String facilityId = facility.getId();
        Call<PageResponse<Queue>> facilityQueuesCall = queueApiService.getAllQueues(facilityId);
        facilityQueuesCall.enqueue(new Callback<PageResponse<Queue>>() {
            @Override
            public void onResponse(Call<PageResponse<Queue>> call, Response<PageResponse<Queue>> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        PageResponse<Queue> queuesPageResp = response.body();
                        if (queuesPageResp != null) {
                            List<Queue> queues = queuesPageResp.getDocs();
                            queueAdapter = new QueueAdapter(queues,
                                    R.layout.list_item_queue,
                                    getApplicationContext(),
                                    new QueueAdapterListener() {
                                        @Override
                                        public void onQueueSelected(int position, Queue queue) {
                                            if (queue != null) {
//                                                QueueService.getInstance().setQueueInstance(queue);
                                                selectedQueuePosition = position;
                                                Intent intent = new Intent(FacilityActivity.this, QueueActivity.class);
                                                intent.putExtra("selected_queue", queue);
                                                startActivityForResult(intent, SELECTED_QUEUE_STATE);
                                            }
                                        }
                                    });
                            queuesRecyclerView.setAdapter(queueAdapter);
                        }
                    } else if (response.errorBody() != null) {
                        RestError.ShowError(TAG, response, getApplicationContext());
                    }
                }
            }

            @Override
            public void onFailure(Call<PageResponse<Queue>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTED_QUEUE_STATE && resultCode == RESULT_OK) {
            Queue changedQueue =  (Queue) data.getExtras().get("queue");
            queueAdapter.setItem(selectedQueuePosition, changedQueue);
        }
    }
}
