package com.owl.crazynote;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    private EditText inputTitle;
    private EditText inputDescription;
    private TextView textViewDate;
    private TextView textViewDateLayout;
    private RelativeLayout reminderLayout;
    private String dateValue = "no";
    private String timeValue = "no";
    private String timeAndDateValue = "no";
    private String extrasTitleValue;
    private String extrasDescritpionValue;
    private String extrasDateValue;
    private int extrasId;
    private TextInputLayout inputLayoutTask;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.UK);
    private Calendar today;
    private Calendar newDate;
    private InstanceNotification instanceNotification;
    private Spinner spinnerDays;
    private String nextFewDays[];
    private ArrayAdapter<String> adapterDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
//        setSpinner();
        findViewById();
        setToolbar();
        receiverTaskActivity();
        setDataInViews();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });
        reminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditTaskActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_layout);
                Button close = (Button) dialog.findViewById(R.id.close);
                Button save = (Button) dialog.findViewById(R.id.save);
//                final TextView date = (TextView) dialog.findViewById(R.id.date_picker);
                final TextView time = (TextView) dialog.findViewById(R.id.time_picker);
                RelativeLayout dateLayout = (RelativeLayout) dialog.findViewById(R.id.date_layout);
                RelativeLayout timeLayout = (RelativeLayout) dialog.findViewById(R.id.time_layout);
                dateLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePicker(v);
                    }
                });
                timeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker(v);
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeAndDateValue = dateValue + " " + timeValue;
                        if (newDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                            textViewDate.setText("Today" + timeValue);
                        } else {
                            textViewDate.setText(dateValue + " " + timeValue);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void setDataInViews() {
        inputTitle.setText(extrasTitleValue);
        inputDescription.setText(extrasDescritpionValue);
        textViewDate.setText(extrasDateValue);
    }

    private void receiverTaskActivity() {
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            extrasTitleValue = extras.getString("title");
            extrasDescritpionValue = extras.getString("description");
            extrasDateValue = extras.getString("date");
            extrasId = extras.getInt("id");
        }
    }

    private void setSpinner() {
        nextFewDays = getResources().getStringArray(R.array.nextFewDays);
        spinnerDays = (Spinner) findViewById(R.id.days);
        adapterDays = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, nextFewDays);
        adapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapterDays);
        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                datePicker(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void findViewById() {
        inputTitle = (EditText) findViewById(R.id.input_title);
        inputDescription = (EditText) findViewById(R.id.input_description);
        inputDescription.setHintTextColor(getResources().getColor(R.color.colorSecondaryText));
        inputLayoutTask = (TextInputLayout) findViewById(R.id.input_layout_title);
        assert inputLayoutTask != null;
        inputLayoutTask.setHint("DataBase");
        textViewDate = (TextView) findViewById(R.id.text_view_date);
        textViewDateLayout = (TextView) findViewById(R.id.date);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);
        reminderLayout = (RelativeLayout) findViewById(R.id.reminder);
    }

    public void addTask(View view) {
        hideKeyboard();
        String noteTitle = inputLayoutTask.getEditText().getText().toString();
        String noteDescription = inputDescription.getText().toString();
        if (!validateTask(noteTitle)) {
            inputLayoutTask.setError("Enter your task!");
        } else {
            Intent intent = new Intent();
            intent.putExtra("title", noteTitle);
            intent.putExtra("date", timeAndDateValue);
            intent.putExtra("description", noteDescription);
            intent.putExtra("colorCircleIcon", colorGenerator());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateTask(String task) {
        return task.trim().length() > 0;
    }

    private int colorGenerator() {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        return generator.getRandomColor();
    }

    public void datePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                today = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateValue = dateFormatter.format(newDate.getTime());
//                view.invalidate();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void timePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newTime;
                        newTime = Calendar.getInstance();
                        newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newTime.set(Calendar.MINUTE, minute);
                        timeValue = timeFormatter.format(newTime.getTime());
//                        view.invalidate();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addtask, menu);
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

            case R.id.add_alert_Button:
//                datePicker(findViewById(android.R.id.content));
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }
}
