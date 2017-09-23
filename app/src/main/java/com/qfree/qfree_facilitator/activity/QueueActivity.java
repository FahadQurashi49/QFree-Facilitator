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
import com.qfree.qfree_facilitator.model.CustomerCallback;
import com.qfree.qfree_facilitator.model.PageResponse;
import com.qfree.qfree_facilitator.model.Queue;
import com.qfree.qfree_facilitator.rest.ApiClient;
import com.qfree.qfree_facilitator.rest.QueueApiInterface;
import com.qfree.qfree_facilitator.rest.RestError;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.List;

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

    private CustomerAdapter customerAdapter;
    private Queue queue;
    private QueueApiInterface queueApiService;
    private boolean queueStateChange = false;
    private Customer frontCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        queueApiService = ApiClient.getClient().create(QueueApiInterface.class);

//        queue = QueueService.getInstance().getQueueInstance();
        queue = (Queue) getIntent().getSerializableExtra("selected_queue");
        queueNameTextView = (TextView) findViewById(R.id.tv_queue_name);
        customersRecyclerView = (RecyclerView) findViewById(R.id.rv_customers);
        runQueueButton = (Button) findViewById(R.id.btn_run_queue);

        queueNameTextView.setText(queue.getName());
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter(R.layout.list_item_customer, getApplicationContext());
        customersRecyclerView.setAdapter(customerAdapter);

        if (queue.getRunning()) {
            customersRecyclerView.setVisibility(View.VISIBLE);

            toggleQueueFunctionalityButtons(true);
            syncCustomerList();

        } else {
            runQueueButton.setVisibility(View.VISIBLE);
        }


    }

    public void onBtnRunQueueClick(View view) {
        Call<Queue> runQueueCall = queueApiService.runQueue(queue.getFacilityId(), queue.getId());
        runQueueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Queue respQueue = response.body();
                        if (respQueue.getRunning()) {
                            Toast.makeText(getApplicationContext(), "Running " + respQueue.getName(), Toast.LENGTH_SHORT).show();
                            runQueueButton.setVisibility(View.INVISIBLE);
                            queue = respQueue;
                            queueStateChange = true;
                            toggleQueueFunctionalityButtons(true);
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
        Call<Queue> cancelQueueCall = queueApiService.cancelQueue(queue.getFacilityId(), queue.getId());
        cancelQueueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Queue respQueue = response.body();
                        if (respQueue != null && !respQueue.getRunning()) {
//                            Toast.makeText(getApplicationContext(), "Cancel " + respQueue.getName(), Toast.LENGTH_SHORT).show();
                            runQueueButton.setVisibility(View.VISIBLE);
                            queue = respQueue;
                            queueStateChange = true;
                            customerAdapter.clearList();

                            toggleQueueFunctionalityButtons(false);
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

    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("queue", queue);
        if (queueStateChange) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED, returnIntent);
        }
        super.finish();
    }

    private void toggleQueueFunctionalityButtons (boolean show) {
        int visibility = show? View.VISIBLE: View.INVISIBLE;
        // show/ hide enqueue, dequeue, cancel buttons
        enqueueButton = (Button) findViewById(R.id.btn_enqueue_customer);
        enqueueButton.setVisibility(visibility);
        dequeueButton = (Button) findViewById(R.id.btn_dequeue_customer);
        dequeueButton.setVisibility(visibility);
        cancelButton = (Button) findViewById(R.id.btn_cancel_queue);
        cancelButton.setVisibility(visibility);

    }

    private void confirmDequeueCustomer() {
        final Context context = this;

        getFrontCustomer(new CustomerCallback() {
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
        Call<Customer> frontCustomerCall = queueApiService.getFrontCustomer(queue.getFacilityId(), queue.getId());
        frontCustomerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Customer respCustomer = response.body();
                        if (respCustomer != null) {
                            frontCustomer = respCustomer;
                            customerCallback.onCustomerReceive(respCustomer);
                        }
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
        Call<Customer> dequeueCustomerCall = queueApiService.dequeueCustomer(queue.getFacilityId(), queue.getId());
        dequeueCustomerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                try {
                    if (RestError.ShowIfError(TAG, response, getApplicationContext())) {
                        Customer dequeuedCustomer = response.body();
                        if (dequeuedCustomer != null) {
                            customerAdapter.removeCustomer(dequeuedCustomer);
                        } else {
                            syncCustomerList();
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
        Call<Queue> queueCall = queueApiService.getQueue(this.queue.getFacilityId(), this.queue.getId());
        queueCall.enqueue(new Callback<Queue>() {
            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                Queue respQueue = response.body();
                if (!EqualsBuilder.reflectionEquals(queue, respQueue)) {
                    queue = respQueue;
                    queueStateChange = true;
                }
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {

            }
        });
    }

    private void syncCustomerList() {
        // populate list with queue customers
        Call<PageResponse<Customer>> allQueueCustomerCall =
                queueApiService.getAllQueueCustomer(queue.getFacilityId(), queue.getId());
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
