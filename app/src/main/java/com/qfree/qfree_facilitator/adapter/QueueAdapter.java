package com.qfree.qfree_facilitator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qfree.qfree_facilitator.R;
import com.qfree.qfree_facilitator.model.Queue;

import java.util.List;

/**
 * Created by Fahad Qureshi on 9/12/2017.
 */

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder>{
    private List<Queue> queues;
    private int rowLayout;
    private Context context;
    private QueueAdapterListener listener;

    public QueueAdapter(List<Queue> queues, int rowLayout, Context context, QueueAdapterListener listener) {
        this.queues = queues;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public QueueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new QueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QueueViewHolder holder, final int position) {
        Queue queue = queues.get(position);
        holder.queueName.setText(queue.getName());
        if (queue.getRunning()) {
            holder.queuesLayout.setBackgroundColor(Color.GREEN);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onQueueSelected(position, queues.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return queues.size();
    }

    public void setItem (int position, Queue queue) {
        this.queues.set(position, queue);
        notifyItemChanged(position);
    }
    public void addItem (Queue queue) {
        this.queues.add(queue);
        notifyDataSetChanged();
    }

    public static class QueueViewHolder extends RecyclerView.ViewHolder {
        LinearLayout queuesLayout;
        TextView queueName;

        public QueueViewHolder(View itemView) {
            super(itemView);
            queuesLayout = (LinearLayout) itemView.findViewById(R.id.layout_li_queues);
            queueName = (TextView) itemView.findViewById(R.id.tv_li_queue_name);
        }
    }

}
