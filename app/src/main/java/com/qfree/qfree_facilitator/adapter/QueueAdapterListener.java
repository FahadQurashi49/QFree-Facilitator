package com.qfree.qfree_facilitator.adapter;

import com.qfree.qfree_facilitator.model.Queue;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public interface QueueAdapterListener {
    void onQueueSelected(int position, Queue queue);
}
