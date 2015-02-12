package mobiledev.club.reminders.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;

public class ReminderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Intent intent = getIntent();
        Reminder reminder = (Reminder) intent.getSerializableExtra("reminder");
        if(reminder == null)
        {
            return;
        }

        TextView nameTextView = (TextView) findViewById(R.id.textview_name);
        TextView descriptionTextView = (TextView) findViewById(R.id.textview_description);
        TextView dateTextView = (TextView) findViewById(R.id.textview_date);

        nameTextView.setText(reminder.getName());
        descriptionTextView.setText(reminder.getDescription());

        Date date = reminder.getDueDate();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
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
}
