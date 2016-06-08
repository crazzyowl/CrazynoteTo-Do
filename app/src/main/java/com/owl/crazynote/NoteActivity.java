package com.owl.crazynote;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView date;
    String titleValue;
    String descriptionValue;
    String dateValue;
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        findViewById();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            titleValue = extras.getString("title");
            descriptionValue = extras.getString("description");
            dateValue = extras.getString("date");
        }
        title.setText(titleValue);
        description.setText(descriptionValue);
        if(!dateValue.equals("noReminder")){
            try {
                date.setText(dateFormatter(dateValue));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            date.setText("Reminder not set");
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private String dateFormatter(String date) throws ParseException {
        DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy kk:mm", Locale.UK);
        DateFormat format2 = new SimpleDateFormat("d, MMMM kk:mm",Locale.UK);
        Date date2 = format1.parse(date);
        return format2.format(date2).toString();
    }
    private void findViewById(){
        title = (TextView) findViewById(R.id.title_text_view);
        description = (TextView) findViewById(R.id.description_text_view);
        date = (TextView) findViewById(R.id.text_view_date);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_edit);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_delete:
                break;

            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }
}
