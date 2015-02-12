package mobiledev.club.reminders.activities;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;

public class NewReminderActivity extends ActionBarActivity {

    private static final String datePrefix = "Due Date: ";

    private int mYear;
    private int mMonth;
    private int mDay;

    private Date dueDate;
    private DateFormat dateFormat;

    private static ArrayList<Reminder> reminders;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        loadReminders();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dueDate = c.getTime();
        dateFormat = SimpleDateFormat.getDateInstance();

        updateDateText();

    }

    private void loadReminders()
    {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.reminders_prefs_key), "");
        reminders = gson.fromJson(json, new TypeToken<List<Reminder>>(){}.getType());
        if(reminders == null)
        {
            reminders = new ArrayList<Reminder>();
        }
//        Toast toast = Toast.makeText(this, reminders.size() + "", Toast.LENGTH_SHORT);
//        toast.show();
    }

    private void saveReminders() {
        Gson gson = new Gson();
        String json = gson.toJson(reminders);
        editor.putString(getString(R.string.reminders_prefs_key), json);
        editor.commit();
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

        newReminder.setName(name);
        newReminder.setDescription(description);
        newReminder.setDueDate(dueDate);

        reminders.add(newReminder);

        saveReminders();

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