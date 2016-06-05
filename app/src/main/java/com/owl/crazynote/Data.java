package com.owl.crazynote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by owl on 21.05.16.
 */
public class Data extends SQLiteOpenHelper {
    private static final int DATA_BASE_VER = 1;
    private static final String TABLE_NAME ="notes";
    public Data(Context context){
        super(context,"tasks.db",null,DATA_BASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes("+"id integer primary key autoincrement,"+"title text,"+"date text,"+"description text,"+"colorCircleIcon integer);"+"");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(db);
    }


    public void addTask(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("date", note.getDate());
        values.put("description", note.getDescription());
        values.put("colorCircleIcon",note.getColorCircleIcon());
        if (note.getId() != 0) values.put("id", note.getId());
        note.setId((int) db.insertOrThrow(TABLE_NAME,null,values));
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
    public List<Note> getAllTask(){
        List<Note> notes = new ArrayList<>();
        String[] columns = {"id", "title","date","description","colorCircleIcon"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,null);
        while (cursor.moveToNext()){
//            DatabaseUtils.dumpCurrentRow(cursor);
            Note note = new Note();
            note.setTitle(cursor.getString(1));
            note.setDate(cursor.getString(2));
            note.setDescription(cursor.getString(3));
            note.setColorCircleIcon(cursor.getInt(4));
            note.setId(cursor.getInt(0));
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    public void updateTask(int id, String title, String date, String description, int colorCircleIcon){
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("description", description);
        values.put("colorCircleIcon",colorCircleIcon);
        String args[]={id+""};
        db.update(TABLE_NAME,values,"id=?",args);
        db.close();
    }

}