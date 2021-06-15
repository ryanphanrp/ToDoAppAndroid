package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseController;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> taskList;
    private final MainActivity activity;

    private final DatabaseController database;

    // Constructor
    public ToDoAdapter(DatabaseController database, MainActivity activity) {
        this.database = database;
        this.activity = activity;
    }

    /*
    *  Handle with View & ViewHolder
    * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.task_checkbox);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        database.openDatabase();

        // Handle with item
        ToDoModel item = taskList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(item.isStatus());

        // Handle when checkbox change
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                database.changeStatus(item.getId(), isChecked);
            }
        });
    }

    public Context getContext() {
        return activity;
    }


    /*
    *  Handle with Adapter & database
    * */
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<ToDoModel> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ToDoModel item = taskList.get(position);
        database.deleteTask(item.getId());
        taskList.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position) {
        ToDoModel item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
}
