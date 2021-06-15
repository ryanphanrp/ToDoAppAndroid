package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController extends SQLiteOpenHelper {

    /* Initial variables */
    private SQLiteDatabase db;
    private Context context;

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        private static final int VERSION = 1;
        private static final String NAME = "taskListDatabase";
        private static final String TODO_TABLE = "todo";
        private static final String ID = "id";
        private static final String TASK = "task";
        private static final String STATUS = "status";
    }

    // Query to create table
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + FeedEntry.TODO_TABLE + "("
            + FeedEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FeedEntry.TASK + " TEXT, "
            + FeedEntry.STATUS + " BOOLEAN)";

    // Query to drop table
    private static final String DROP_TODO_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TODO_TABLE;

    // Constructor
    public DatabaseController(Context context) {
        super(context, FeedEntry.NAME, null, FeedEntry.VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*This database is only a cache for online data, so its upgrade policy is
        to simply to discard the data and start over*/
        db.execSQL(DROP_TODO_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    /*
    Action with tasks
    */
    public void openDatabase() {
        // Gets the data repository in write mode
        db = this.getWritableDatabase();
    }

    public List<ToDoModel> getAllTask() {
        List<ToDoModel> taskList = new ArrayList<>();

        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(FeedEntry.TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(FeedEntry.ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(FeedEntry.TASK)));
                        task.setStatus(Boolean.parseBoolean(cur.getString(cur.getColumnIndex(FeedEntry.STATUS))));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void insertTask(ToDoModel task) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.TASK, task.getTask());
        values.put(FeedEntry.STATUS, task.isStatus());

        String queryString = "select * from " + FeedEntry.TODO_TABLE + " where " + FeedEntry.TASK + " =?";
        Cursor cursor = db.rawQuery(queryString, new String[] {task.getTask()});
        if(cursor.moveToFirst()){
            Toast.makeText(context, "This task is existed.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert new task to database
        db.insert(FeedEntry.TODO_TABLE, null, values);
        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
    }

    public void deleteTask(int Id) {
        String selection = FeedEntry.ID + "=?";
        String[] selectionArgs = {String.valueOf(Id)};
        db.delete(FeedEntry.TODO_TABLE, selection, selectionArgs);
    }

    public void updateTask(int Id, String task) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.TASK, task);
        db.update(FeedEntry.TODO_TABLE, values,FeedEntry.ID + "=?", new String[] {String.valueOf(Id)});
    }

    public void changeStatus(int Id, boolean status) {
        ContentValues values = new ContentValues();
        values.put(FeedEntry.STATUS, status);
        db.update(FeedEntry.TODO_TABLE, values,FeedEntry.ID + "=?", new String[] {String.valueOf(Id)});
    }
}
