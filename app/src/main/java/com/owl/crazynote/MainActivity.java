package com.owl.crazynote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1;
    Data dataBase = new Data(this);
    String taskValue;
    String taskData;
    private Task lastTask = null;
    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    FloatingActionButton fabAddTask;


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            lastTask = taskList.get(position);
            dataBase.deleteTask(taskList.get(position).getId());
            taskList.remove(position);
            taskAdapter.notifyDataSetChanged();
            showTaskInLog();
                Snackbar.make(recyclerView, "1 task removed", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataBase.addTask(lastTask);
                        taskList.add(lastTask);
                        Collections.sort(taskList);
                        taskAdapter.notifyItemInserted(taskList.indexOf(lastTask));
                        recyclerView.requestLayout();
                    }
                }).show();
        }
    };
    private List<Task> addSomeTask(){
        List<Task> testListTask = new ArrayList<>();
        Task testTask;
        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);
        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        testTask = new Task("Co by tu można kupić ","jutro");
        testListTask.add(testTask);

        return testListTask;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab button
        fabAddTask = (FloatingActionButton) findViewById(R.id.fab_add_task);
        assert fabAddTask != null;
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInRightAnimator());

        taskList = dataBase.getAllTask();
        taskList = addSomeTask();
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

        showTaskInLog();
//        dataBase.deleteAll();
//        dislpayTaskInLog();
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }
    private void showTaskInLog() {

        for (Task t : dataBase.getAllTask()) {
            Log.d("Dane z bazy:", t.toString());
        }
    }


    //    taskList.remove(position);
//    dataBase.deleteTask(position);
//    viewHolder.setIsRecyclable(false);
//    taskAdapter.notifyItemRemoved(position);
//    taskAdapter.notifyDataSetChanged();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK) return;
        Bundle reciverData = data.getExtras();
        taskValue = reciverData.getString("task");
        taskData = reciverData.getString("data");
        Task task = new Task(taskValue, taskData);
        dataBase.addTask(task);
        taskList.add(task);
        taskAdapter.notifyItemInserted(taskAdapter.getItemCount()-1);
        recyclerView.requestLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_about_app:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;

            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;

            default:
                break;
        }

        return true;
    }
}
