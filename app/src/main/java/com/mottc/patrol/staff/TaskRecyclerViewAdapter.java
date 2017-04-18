package com.mottc.patrol.staff;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mottc.patrol.Constant;
import com.mottc.patrol.R;
import com.mottc.patrol.data.entity.Task;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private final TaskFragment.OnTaskListClickListener mListener;

    public TaskRecyclerViewAdapter(List<Task> items, TaskFragment.OnTaskListClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mContentView.setText(mValues.get(position).getTime());

        if (mValues.get(position).getStatus() == Constant.TASK_STATUS_UNDONE) {
            holder.mStatus.setText("未完成");
            holder.mStatus.setTextColor(Color.RED);
        } else {
            holder.mStatus.setText("已完成");
            holder.mStatus.setTextColor(Color.BLACK);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnTaskListClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mStatus;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStatus = (TextView) view.findViewById(R.id.status);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
