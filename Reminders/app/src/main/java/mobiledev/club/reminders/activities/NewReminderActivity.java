package mobiledev.club.reminders.activities;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;
import mobiledev.club.reminders.notification.ScheduleClient;
import mobiledev.club.reminders.sqlite.RemindersDataSource;

public class NewReminderActivity extends ActionBarActivity {

    private static final String datePrefix = "Due Date: ";
    private static final String timePrefix = "Due Time: ";

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private Date dueDate;
    private DateFormat dateFormat;
    private DateFormat timeFormat;

    // This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        dueDate = c.getTime();
        dateFormat = SimpleDateFormat.getDateInstance();
        timeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

        updateDateText();
        updateTimeText();

    }

    private void saveReminder(Reminder reminder) {

        RemindersDataSource datasource = new RemindersDataSource(this);
        datasource.open();
        datasource.createReminder(reminder);
        datasource.close();

        long timeInMs = reminder.getDueDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMs);

        //The schedule client handles setting up when the reminder goes off
        scheduleClient.setAlarmForNotification(reminder.getTitle(), calendar);

        //Makes toast:
        Toast.makeText(this, "Reminder set for " + SimpleDateFormat.getDateTimeInstance().format(new Date(timeInMs)), Toast.LENGTH_LONG).show();
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
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                dueDate = cal.getTime();
                NewReminderActivity.this.updateDateText();
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    public void timeButtonOnClick(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                Calendar cal = Calendar.getInstance(Locale.US);
                cal.set(mYear, mMonth, mDay, hour, minute);
                mHour = hour;
                mMinute = minute;
                dueDate = cal.getTime();
                NewReminderActivity.this.updateTimeText();
            }
        }, mHour, mMinute, false);
        dialog.show();
    }

    private void updateDateText() {
        String dueDateString = dateFormat.format(dueDate);
        TextView dateTextView = (TextView) findViewById(R.id.textview_date);
        dateTextView.setText(datePrefix + dueDateString);
    }

    private void updateTimeText() {
        String dueTimeString = timeFormat.format(dueDate);
        TextView timeTextView = (TextView) findViewById(R.id.textview_time);
        timeTextView.setText(timePrefix + dueTimeString);
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

    /**
     * Overridden so we can unbind the schedule client
     */
    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        //  This stops us leaking our activity into the system
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}