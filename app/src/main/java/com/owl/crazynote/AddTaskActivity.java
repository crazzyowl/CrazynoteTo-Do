package com.owl.crazynote;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends AppCompatActivity {
    EditText editTextTask;
    EditText editTextData;
    Button button;
    String taskValue;
    String dataValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        editTextTask = (EditText) findViewById(R.id.add_task_task);
        editTextData = (EditText) findViewById(R.id.add_task_data);
    }
    public void addTask(View view){
        taskValue = editTextTask.getText().toString();
        dataValue = editTextData.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("task",taskValue);
        intent.putExtra("data",dataValue);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
