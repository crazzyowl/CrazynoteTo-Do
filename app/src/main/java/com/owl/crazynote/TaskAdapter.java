package com.owl.crazynote;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    //source data
    private List<Task> taskItems;
    public TaskAdapter(List<Task> taskItems) {
        this.taskItems = taskItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        final Task task = taskItems.get(position);
        viewHolder.task.setText(task.getTask());
//        viewHolder.date.setText(task.getDate());
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, task.getTask(), Snackbar.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }


    public final static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView task;
//        public TextView date;

        public MyViewHolder(View view) {
            super(view);
            task = (TextView) view.findViewById(R.id.task);
//            date = (TextView) view.findViewById(R.id.date);
        }
    }
}