package com.owl.crazynote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by owl on 21.05.16.
 */
public class Data extends SQLiteOpenHelper {
    private static final int DATA_BASE_VER = 1;
    private static final String TABLE_NAME ="tasks";
    public Data(Context context){
        super(context,"tasks.db",null,DATA_BASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tasks("+"id integer primary key autoincrement,"+"task text,"+"date text);"+"");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+"tasks");
        onCreate(db);
    }

    public void addTask(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task", task.getTask());
        values.put("date", task.getDate());
        if (task.getId() != 0) values.put("id", task.getId());
        task.setId((int) db.insertOrThrow(TABLE_NAME,null,values));
        db.close();
    }
    public void deleteTask(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] argument ={String.valueOf(id)};
        db.delete(TABLE_NAME,"id=?",argument);
//        db.execSQL("DELETE FROM tasks WHERE id='"+id+"';");
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME);
        db.close();
    }
    public List<Task> getAllTask(){
        List<Task> tasks = new ArrayList<>();
        String[] columns = {"id", "task","date"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,null);
        while (cursor.moveToNext()){
//            DatabaseUtils.dumpCurrentRow(cursor);
            Task task = new Task();
            task.setTask(cursor.getString(1));
            task.setDate(cursor.getString(2));
            task.setId(cursor.getInt(0));
            tasks.add(task);
        }
        cursor.close();
        return tasks;
    }

    public void updateTask(int id, String task, String date){
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task", task);
        values.put("date", date);
        String args[]={id+""};
        db.update(TABLE_NAME,values,"id=?",args);
        db.close();
    }

}