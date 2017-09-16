package com.qfree.qfree_facilitator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Queue;
import com.qfree.qfree_facilitator.service.QueueService;

public class QueueActivity extends AppCompatActivity {

    private TextView queueNameTextView;

    private Queue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        queue = QueueService.getInstance().getQueueInstance();
        queueNameTextView = (TextView) findViewById(R.id.tv_queue_name);
        queueNameTextView.setText(queue.getName());

    }
}
