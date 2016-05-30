package com.owl.crazynote;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends AppCompatActivity {
    EditText editTextTask;
    EditText editTextDate;
    Button button;
    String taskValue;
    String dateValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        editTextTask = (EditText) findViewById(R.id.add_task_task);
        editTextDate = (EditText) findViewById(R.id.add_task);
    }
    public void addTask(View view){
        taskValue = editTextTask.getText().toString();
        dateValue = editTextDate.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("task",taskValue);
        intent.putExtra("date", dateValue);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
