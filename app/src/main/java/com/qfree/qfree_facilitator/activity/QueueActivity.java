package com.qfree.qfree_facilitator.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import com.qfree.qfree_facilitator.callbacks.CustomerCallback;
import com.qfree.qfree_facilitator.model.PageResponse;
import com.qfree.qfree_facilitator.model.Queue;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.QueueApiInterface;
import com.qfree.qfree_facilitator.rest.RestError;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueActivity extends AppCompatActivity {
    private static final String TAG = QueueActivity.class.getSimpleName();

    private TextView queueNameTextView;
    private RecyclerView customersRecyclerView;
    private Button runQueueButton;
    private Button enqueueButton;
    private Button dequeueButton;
    private Button cancelButton;
    private TextView frontTextView;
    private TextView rearTextView;
    private TextView frontLabel;
    private TextView rearLabel;
    private TextView nextCustomerLabel;
    private TextView nextCustomerTextView;

    private CustomerAdapter customerAdapter;
    private Queue currQueue;
    private QueueApiInterface queueApiService;
    private boolean queueStateChange = false;
    private Customer frontCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        queueApiService = ApiClient.getClient().create(QueueApiInterface.class);

//        queue = QueueService.getInstance().getQueueInstance();
        currQueue = (Queue) getIntent().getSerializableExtra("selected_queue");
        queueNameTextView = (TextView) findViewById(R.id.tv_queue_name);
        customersRecyclerView = (RecyclerView) findViewById(R.id.rv_customers);
        runQueueButton = (Button) findViewById(R.id.btn_run_queue);
        enqueueButton = (Button) findViewById(R.id.btn_enqueue_customer);
        dequeueButton = (Button) findViewById(R.id.btn_dequeue_customer);
        cancelButton = (Button) findViewById(R.id.btn_cancel_queue);
        frontTextView = (TextView) findViewById(R.id.tv_front);
        rearTextView = (TextView) findViewById(R.id.tv_rear);
        frontLabel = (TextView) findViewById(R.id.lbl_front);
        rearLabel = (TextView) findViewById(R.id.lbl_rear);
        nextCustomerLabel = (TextView) findViewById(R.id.lbl_next_customer);
        nextCustomerTextView = (TextView) findViewById(R.id.tv_next_customer);

        queueNameTextView.setText(currQueue.getName());
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(R.layout.list_item_customer, getApplicationContext());
        customersRecyclerView.setAdapter(customerAdapter);

        if (currQueue.getRunning()) {
            customersRecyclerView.setVisibility(View.VISIBLE);

            toggleQueueFunctionalityViews(true);
            syncCustomerList();
            syncQueueUI();

        } else {
            runQueueButton.setVisibility(View.VISIBLE);
            toggleQueueFunctionalityViews(false);
        }


    }

    public void onBtnRunQueueClick(View view) {
        Call<Queue> runQueueCall = queueApiService.runQueue(currQueue.getFacilityId(), currQueue.getId());
        runQueueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Queue respQueue = response.body();
                        if (respQueue.getRunning()) {
                            Toast.makeText(getApplicationContext(), "Running " + respQueue.getName(), Toast.LENGTH_SHORT).show();
                            runQueueButton.setVisibility(View.INVISIBLE);
                            currQueue = respQueue;
                            queueStateChange = true;
                            toggleQueueFunctionalityViews(true);
                            syncQueueUI();
                        }
                    }
                } catch (Exception e) {
                    RestError.ShowError(TAG, e.getMessage(), getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                RestError.ShowError(TAG, t.toString(), getApplicationContext());
            }
        });
    }

    public void onBtnCancelQueueClick(View view) {
        Call<Queue> cancelQueueCall = queueApiService.cancelQueue(currQueue.getFacilityId(), currQueue.getId());
        cancelQueueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Queue respQueue = response.body();
                        if (respQueue != null && !respQueue.getRunning()) {
//                            Toast.makeText(getApplicationContext(), "Cancel " + respQueue.getName(), Toast.LENGTH_SHORT).show();
                            runQueueButton.setVisibility(View.VISIBLE);
                            currQueue = respQueue;
                            queueStateChange = true;
                            customerAdapter.clearList();

                            toggleQueueFunctionalityViews(false);
                        }
                    }
                } catch (Exception e) {
                    RestError.ShowError(TAG, e.getMessage(), getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                RestError.ShowError(TAG, t.toString(), getApplicationContext());
            }
        });

    }
    public void onBtnDequeueClick(View view) {
        confirmDequeueCustomer();
    }

    public void onBtnEnqueueClick(View view) {
        enqueueDummyCustomer();
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("queue", currQueue);
        if (queueStateChange) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED, returnIntent);
        }
        super.finish();
    }

    private void toggleQueueFunctionalityViews(boolean show) {
        int visibility = show? View.VISIBLE: View.INVISIBLE;
        // show/ hide enqueue, dequeue, cancel buttons
        enqueueButton.setVisibility(visibility);
        dequeueButton.setVisibility(visibility);
        cancelButton.setVisibility(visibility);
        frontLabel.setVisibility(visibility);
        rearLabel.setVisibility(visibility);
        frontTextView.setVisibility(visibility);
        rearTextView.setVisibility(visibility);
        nextCustomerLabel.setVisibility(visibility);
        nextCustomerTextView.setVisibility(visibility);
        if(!show) {
            frontTextView.setText("");
            rearTextView.setText("");
            nextCustomerTextView.setText("");
        }
    }

    private void syncQueueUI() {
        frontTextView.setText(String.format(Locale.ENGLISH, "%d", currQueue.getFront()));
        rearTextView.setText(String.format(Locale.ENGLISH, "%d", currQueue.getRear()));
        getFrontCustomer(new CustomerCallback() {
            @Override
            public void onError(Response response) {
                if (response != null && response.errorBody() != null)
                    try {
                        RestError errorResponse = ApiClient.getErrorResponse(response.errorBody());
                        if(errorResponse.getErrCode().equals("255")){
                            nextCustomerLabel.setText(errorResponse.getErrMsg());
                            nextCustomerTextView.setVisibility(View.INVISIBLE);
                            return;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                nextCustomerTextView.setText("");
                nextCustomerTextView.setVisibility(View.INVISIBLE);
                nextCustomerLabel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(String errStr) {
                nextCustomerTextView.setText("");
                nextCustomerTextView.setVisibility(View.INVISIBLE);
                nextCustomerLabel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCustomerReceive(Customer customer) {
                if (nextCustomerTextView.getVisibility() == View.INVISIBLE) {
                    nextCustomerLabel.setText(R.string.lbl_next_customer);
                    nextCustomerTextView.setVisibility(View.VISIBLE);
                    nextCustomerLabel.setVisibility(View.VISIBLE);
                }
                nextCustomerTextView.setText(customer.getName());

            }
        });
    }

    private void confirmDequeueCustomer() {
        final Context context = this;

        getFrontCustomer(new CustomerCallback() {
            @Override
            public void onError(Response response) {
                RestError.ShowIfError(TAG, response, getApplicationContext());
            }

            @Override
            public void onError(String errStr) {
                RestError.ShowError(TAG, errStr, getApplicationContext());
            }

            @Override
            public void onCustomerReceive(final Customer customer) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Dequeue Customer")
                        .setMessage("Are you sure you want to Dequeue " + customer.getName() + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dequeueCustomer(customer);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    private void  getFrontCustomer(final CustomerCallback customerCallback) {
        Call<Customer> frontCustomerCall = queueApiService.getFrontCustomer(currQueue.getFacilityId(), currQueue.getId());
        frontCustomerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                try {
                    if (!RestError.IsResponseError(response)) {
                        Customer respCustomer = response.body();
                        if (respCustomer != null) {
                            frontCustomer = respCustomer;
                            customerCallback.onCustomerReceive(respCustomer);
                        }
                    } else {
                        customerCallback.onError(response);
                    }
                } catch (Exception e) {
                    customerCallback.onError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                customerCallback.onError(t.toString());
            }
        });
    }

    private void enqueueDummyCustomer () {
        Call<Customer> enqueueDummyCustomerCall = queueApiService
                .enqueueDummyCustomer(currQueue.getFacilityId(),
                        currQueue.getId());
        enqueueDummyCustomerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Customer enqueuedCustomer = response.body();

                        if (enqueuedCustomer != null) {
                            if(customersRecyclerView.getVisibility() == View.INVISIBLE) {
                                customersRecyclerView.setVisibility(View.VISIBLE);
                            }
                            customerAdapter.addCustomer(enqueuedCustomer);
                        }

                        syncQueueObject();
                    }
                } catch (Exception e) {
                    RestError.ShowError(TAG, e.getMessage(), getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                RestError.ShowError(TAG, t.toString(), getApplicationContext());
            }
        });
    }

    private void dequeueCustomer (final Customer frontCustomer) {
        Call<Customer> dequeueCustomerCall = queueApiService.dequeueCustomer(currQueue.getFacilityId(), currQueue.getId());
        dequeueCustomerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Customer dequeuedCustomer = response.body();
                        if (dequeuedCustomer != null) {
                            customerAdapter.removeCustomer(dequeuedCustomer);
                        }

                        syncQueueObject();

                    }
                } catch (Exception e) {
                    RestError.ShowError(TAG, e.getMessage(), getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                RestError.ShowError(TAG, t.toString(), getApplicationContext());
            }
        });
    }

    private void syncQueueObject () {
        Call<Queue> queueCall = queueApiService.getQueue(this.currQueue.getFacilityId(), this.currQueue.getId());
        queueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                Queue respQueue = response.body();
                if (!EqualsBuilder.reflectionEquals(currQueue, respQueue)) {
                    currQueue = respQueue;
                    queueStateChange = true;
                    syncQueueUI();
                }
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                RestError.ShowError(TAG, t.toString(), getApplicationContext());
            }
        });
    }

    private void syncCustomerList() {
        // populate list with queue customers
        Call<PageResponse<Customer>> allQueueCustomerCall =
                queueApiService.getAllQueueCustomer(currQueue.getFacilityId(), currQueue.getId());
        allQueueCustomerCall.enqueue(new Callback<PageResponse<Customer>>() {
            @Override
            public void onResponse(Call<PageResponse<Customer>> call, Response<PageResponse<Customer>> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        PageResponse<Customer> customerPageResponse = response.body();
                        if (customerPageResponse != null) {
                            List<Customer> queueCustomers = customerPageResponse.getDocs();
                            customerAdapter.setCustomers(queueCustomers);
                        }
                    }
                } catch (Exception e) {
                    RestError.ShowError(TAG, e.getMessage(), getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<PageResponse<Customer>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
