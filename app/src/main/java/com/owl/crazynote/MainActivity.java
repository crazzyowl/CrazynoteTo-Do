package com.owl.crazynote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1;
    final Context context = this;
    private static final int VERTICAL_ITEM_SPACE = 2;
    private Data dataBase = new Data(this);
    private String noteTitle;
    private String noteDate;
    private String noteDescription;
    private int colorCircleIcon;
    private FloatingActionButton fabAddTask;
    private FloatingActionButton fabAddTaskDialog;
    private FloatingActionMenu floatingActionMenu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Note lastNote = null;
    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private Paint paint = new Paint();
    private InputMethodManager inputMethodManager;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            lastNote = noteList.get(position);
            dataBase.deleteTask(noteList.get(position).getId());
            noteList.remove(position);
            taskAdapter.notifyDataSetChanged();
            showTasksInLog();
            Snackbar.make(recyclerView, "1 note removed", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataBase.addTask(lastNote);
                    noteList.add(lastNote);
                    Collections.sort(noteList);
                    taskAdapter.notifyItemInserted(noteList.indexOf(lastNote));
                    recyclerView.requestLayout();
                }
            }).show();
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View view = viewHolder.itemView;
//                float height = (float) view.getBottom() - (float) view.getTop();
                float height = view.getHeight();
                float width = height / 3;
                if (dX > 0) {
                   paint.setColor(Color.parseColor("#388E3C"));
                    RectF background = new RectF((float) view.getLeft(), (float) view.getTop(), dX, (float) view.getBottom());
                    c.drawRect(background, paint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.done_white);
                    RectF icon_dest = new RectF((float) view.getLeft() + width, (float) view.getTop() + width, (float) view.getLeft() + 2 * width, (float) view.getBottom() - width);
                  c.drawBitmap(icon, null, icon_dest, paint);
                }
            }
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Fab buttons
        fabAddTask = (FloatingActionButton) findViewById(R.id.fab_add_task);
        assert fabAddTask != null;
        //Fab menu
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        assert floatingActionMenu != null;
        floatingActionMenu.setClosedOnTouchOutside(true);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                //close fab menu after click
                floatingActionMenu.close(true);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh(){
                    refreshRecycleView();
                    Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //Recycle view decoration
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInRightAnimator());

        //add to database and refresh Recycler View
        refreshRecycleView();
        showTasksInLog();
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK) return;
        Bundle receiverData = data.getExtras();
        noteTitle = receiverData.getString("title");
        noteDate = receiverData.getString("date");
        noteDescription =receiverData.getString("description");
        colorCircleIcon = receiverData.getInt("colorCircleIcon");
        Note note = new Note(noteTitle, noteDate, noteDescription,colorCircleIcon);
        dataBase.addTask(note);
        noteList.add(note);
        taskAdapter.notifyItemInserted(taskAdapter.getItemCount() - 1);
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
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;

            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            default:
                break;
        }
        return true;
    }
    private void showTasksInLog() {

        for (Note t : dataBase.getAllTask()) {
            Log.d("Dane z bazy:", t.toString());
        }
    }
    private void refreshRecycleView() {
        noteList = dataBase.getAllTask();
        taskAdapter = new TaskAdapter(noteList,this);
        recyclerView.setAdapter(taskAdapter);
    }
}
