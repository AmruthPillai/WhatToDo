package com.amruthpillai.whattodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private List<Task> mTasks;

    public TaskAdapter(Context context, List<Task> objects) {
        super(context, R.layout.task_row_item, objects);
        this.mContext = context;
        this.mTasks = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.task_row_item, null);
        }

        Task currentTask = mTasks.get(position);

        TextView textTaskTitle = (TextView) convertView.findViewById(R.id.text_taskTitle);
        TextView textTaskDescription = (TextView) convertView.findViewById(R.id.text_taskDescription);

        textTaskTitle.setText(currentTask.getTitle());
        textTaskDescription.setText(currentTask.getDescription());

        return convertView;
    }

}
