package mobiledev.club.reminders.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;
import mobiledev.club.reminders.sqlite.RemindersDataSource;

public class ReminderActivity extends ActionBarActivity {

    private Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Intent intent = getIntent();
        reminder = (Reminder) intent.getSerializableExtra("reminder");
        if(reminder == null)
        {
            return;
        }


        TextView nameTextView = (TextView) findViewById(R.id.textview_name);
        TextView descriptionTextView = (TextView) findViewById(R.id.textview_description);
        TextView dateTextView = (TextView) findViewById(R.id.textview_date);

        nameTextView.setText(reminder.getTitle());
        descriptionTextView.setText(reminder.getDescription());

        Date date = reminder.getDueDate();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String dateString = dateFormat.format(date);
        dateTextView.setText(dateString);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
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

    public void deleteReminder(View v)
    {
        RemindersDataSource datasource = new RemindersDataSource(this);
        datasource.open();
        datasource.deleteReminder(reminder);
        Toast toast = Toast.makeText(this, "Reminder successfully deleted", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }
}
