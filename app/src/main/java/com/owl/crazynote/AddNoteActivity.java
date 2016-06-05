package com.owl.crazynote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    private EditText inputTitle;
    private EditText inputDescription;
    private TextView textViewDate;
    private String dateValue="noReminder";
    private TextInputLayout inputLayoutTask;
    private TextInputLayout inputLayoutDescription;
    private Toolbar toolbar;
    private SimpleDateFormat dateFormatter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        findViewById();
        setSupportActionBar(toolbar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(v);
            }
        });
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
    }

    private void findViewById() {
        inputTitle = (EditText) findViewById(R.id.input_title);
        inputDescription =(EditText) findViewById(R.id.input_description);
        inputDescription.setHintTextColor(getResources().getColor(R.color.colorSecondaryText));
        textViewDate = (TextView) findViewById(R.id.text_view_date);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);
        inputLayoutTask = (TextInputLayout) findViewById(R.id.input_layout_title);
//        inputLayoutDescription =(TextInputLayout) findViewById(R.id.input_layout_description);
        inputTitle.addTextChangedListener(new MyTextWatcher(inputTitle));
        inputDescription.addTextChangedListener(new MyTextWatcher(inputDescription));

    }

    public void addTask(View view) {
        String noteTitle = inputTitle.getText().toString();
        String noteDescription = inputDescription.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("title", noteTitle);
        intent.putExtra("date", dateValue);
        intent.putExtra("description",noteDescription);
        intent.putExtra("colorCircleIcon",colorGenerator());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    private int colorGenerator(){
        ColorGenerator generator = ColorGenerator.MATERIAL;
        return generator.getRandomColor();
    }
    public void datePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if(newDate.get(Calendar.DAY_OF_MONTH)==today.get(Calendar.DAY_OF_MONTH)){
                    textViewDate.setText("Today");
                    dateValue = dateFormatter.format(newDate.getTime());
                }else{
                    String day = newDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,Locale.UK);
                    textViewDate.setText(dateFormatter.format(newDate.getTime()));
                    dateValue = dateFormatter.format(newDate.getTime());
                }
                view.invalidate();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

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
            default:
                break;
        }
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
//    private void submitForm(){
//        if(!validateTitle()) {
//            return;
//        }
//    }
    private boolean validateTitle() {
        if (inputTitle.getText().toString().trim().isEmpty()) {
//            inputLayoutTask.setError(getString(R.string.err_msg_task));
            requestFocus(inputTitle);
            return false;
        } else {
            inputLayoutTask.setErrorEnabled(false);
        }
        return true;
    }
//    private boolean validateDescription() {
//        if (inputDescription.getText().toString().trim().isEmpty()) {
////            inputLayoutDescription.setError(getString(R.string.err_msg_task));
//            requestFocus(inputDescription);
//            return false;
//        } else {
//            inputLayoutDescription.setErrorEnabled(false);
//        }
//        return true;
//    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_title:
                    validateTitle();
                    break;
//                case R.id.input_description:
//                    validateDescription();
//                    break;
            }
        }
    }
}
