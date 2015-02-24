package mobiledev.club.reminders.activities;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;
import mobiledev.club.reminders.sqlite.RemindersDataSource;

public class NewReminderActivity extends ActionBarActivity {

    private static final String datePrefix = "Due Date: ";

    private int mYear;
    private int mMonth;
    private int mDay;

    private Date dueDate;
    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dueDate = c.getTime();
        dateFormat = SimpleDateFormat.getDateInstance();

        updateDateText();

    }

    private void saveReminder(Reminder reminder) {

        RemindersDataSource datasource = new RemindersDataSource(this);
        datasource.open();
        datasource.createReminder(reminder);
        datasource.close();
    }

    public void saveButtonOnClick(View view) {
        /*
        Toast toast = Toast.makeText(NewReminderActivity.this, "Button Pressed", Toast.LENGTH_LONG);
        toast.show();*/

        EditText nameTextView = (EditText) findViewById(R.id.edittext_name);
        String name = nameTextView.getText().toString();

        EditText descriptionTextView = (EditText) findViewById(R.id.edittext_description);
        String description = descriptionTextView.getText().toString();

        if (name.isEmpty() || description.isEmpty()) {
            Toast toast = Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Reminder newReminder = new Reminder();

        newReminder.setTitle(name);
        newReminder.setDescription(description);
        newReminder.setDueDate(dueDate);

        //Save to database

        saveReminder(newReminder);

        NewReminderActivity.this.finish();
    }

    public void dateButtonOnClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance(Locale.US);
                cal.set(year, monthOfYear, dayOfMonth);
                dueDate = cal.getTime();
                NewReminderActivity.this.updateDateText();
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    private void updateDateText() {
        String dueDateString = dateFormat.format(dueDate);
        TextView dateTextView = (TextView) findViewById(R.id.textview_date);
        dateTextView.setText(datePrefix + dueDateString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}