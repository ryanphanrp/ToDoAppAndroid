package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseController;
import com.example.todoapp.Utils.DialogCloseListener;
import com.example.todoapp.Utils.OnSwipeTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    // Initial View
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton newTaskBtn;

    // Create task list
    private List<ToDoModel> taskList;

    // Create Database
    private DatabaseController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial task list
        taskList = new ArrayList<>();

        // Initial Database Controller
        db = new DatabaseController(this);
        db.openDatabase();  // open database

        // Hidden Action Bar
        assert getSupportActionBar() != null;
        getSupportActionBar().hide();


        // Recycler View to show list task
        tasksRecyclerView = findViewById(R.id.taskRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initial Adapter & view
        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        // Attach Swipe to Recycler View
        ItemTouchHelper.Callback itemTouchHelperCallback;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new OnSwipeTouchListener(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        /*
        * Do something with adapter
        * */
        // Initial example task
        ToDoModel example = new ToDoModel();
        example.setId(1);
        example.setTask("This is an example task");
        example.setStatus(false);

        // Show task list
        taskList = db.getAllTask();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);


        /*
        *  Add new task
        * */
        // Add new task button
        View view;
        newTaskBtn = findViewById(R.id.new_task_button);
        // handle when click add new task button
        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {    //  Handle list task when dialog close
        taskList = db.getAllTask();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}