package com.qfree.qfree_facilitator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.adapter.CustomerAdapter;
import com.qfree.qfree_facilitator.model.Customer;
import com.qfree.qfree_facilitator.model.PageResponse;
import com.qfree.qfree_facilitator.model.Queue;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.QueueApiInterface;
import com.qfree.qfree_facilitator.rest.RestError;
import com.qfree.qfree_facilitator.service.QueueService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueActivity extends AppCompatActivity {
    private static final String TAG = QueueActivity.class.getSimpleName();

    private TextView queueNameTextView;
    private RecyclerView customersRecyclerView;
    private Button runQueueButton;

    private CustomerAdapter customerAdapter;
    private Queue queue;
    private QueueApiInterface queueApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        queueApiService = ApiClient.getClient().create(QueueApiInterface.class);

//        queue = QueueService.getInstance().getQueueInstance();
        queue = (Queue) getIntent().getSerializableExtra("selected_queue");
        queueNameTextView = (TextView) findViewById(R.id.tv_queue_name);

        queueNameTextView.setText(queue.getName());

        if (queue.getRunning()) {
            customersRecyclerView = (RecyclerView) findViewById(R.id.rv_customers);
            customersRecyclerView.setVisibility(View.VISIBLE);
            customersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            Call<PageResponse<Customer>> allQueueCustomerCall =
                    queueApiService.getAllQueueCustomer(queue.getFacilityId(), queue.getId());
            allQueueCustomerCall.enqueue(new Callback<PageResponse<Customer>>() {
                @Override
                public void onResponse(Call<PageResponse<Customer>> call, Response<PageResponse<Customer>> response) {

                    if (response.isSuccessful()) {
                        PageResponse<Customer> customerPageResponse = response.body();
                        if (customerPageResponse != null) {
                            List<Customer> queueCustomers = customerPageResponse.getDocs();
                            customerAdapter = new CustomerAdapter(queueCustomers, R.layout.list_item_customer, getApplicationContext());
                            customersRecyclerView.setAdapter(customerAdapter);
                        }

                    } else if (response.errorBody() != null) {
                        RestError.ShowError(TAG, response, getApplicationContext());
                    }
                }

                @Override
                public void onFailure(Call<PageResponse<Customer>> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            runQueueButton = (Button) findViewById(R.id.btn_run_queue);
            runQueueButton.setVisibility(View.VISIBLE);

        }


    }

    public void onBtnRunQueueClick(View view) {
        Call<Queue> runQueueCall = queueApiService.runQueue(queue.getFacilityId(), queue.getId());
        runQueueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                if (response.isSuccessful()) {
                    Queue respQueue = response.body();
                    if (respQueue.getRunning()) {
                        Toast.makeText(getApplicationContext(), "Running " + respQueue.getName(), Toast.LENGTH_SHORT).show();
                        runQueueButton.setVisibility(View.INVISIBLE);
                        queue = respQueue;
                    }

                } else if (response.errorBody() != null) {
                    RestError.ShowError(TAG, response, getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {

            }
        });
    }
}
