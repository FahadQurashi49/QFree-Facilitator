package com.qfree.qfree_facilitator.service;

import com.qfree.qfree_facilitator.model.Queue;

/**
 * Created by Fahad Qureshi on 9/16/2017.
 */

public class QueueService {
    private static final QueueService ourInstance = new QueueService();
    private static final Queue queueInstance = new Queue();

    public static QueueService getInstance() {
        return ourInstance;
    }

    private QueueService() {
    }

    public Queue getQueueInstance () {
        return queueInstance;
    }
    public void setQueueInstance (Queue queue) {
        queueInstance.setId(queue.getId());
        queueInstance.setName(queue.getName());
        queueInstance.setRunning(queue.getRunning());
        queueInstance.setRear(queue.getRear());
        queueInstance.setFront(queue.getFront());
        queueInstance.setFacilityId(queue.getFacilityId());
    }
}
