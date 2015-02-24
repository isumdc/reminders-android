package mobiledev.club.reminders.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.adapters.ArrayAdapterNewReminder;
import mobiledev.club.reminders.models.Reminder;
import mobiledev.club.reminders.sqlite.RemindersDataSource;


public class MainActivity extends ActionBarActivity {

    private static ArrayList<Reminder> reminders;
    private ListView listView;
    private static ArrayAdapterNewReminder adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadReminders();

        listView = (ListView)findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reminder reminder = reminders.get(position);
                Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
                intent.putExtra("reminder", reminder);
                MainActivity.this.startActivity(intent);
            }
        });
        adapter = new ArrayAdapterNewReminder(this, R.layout.item_reminder, reminders);
        listView.setAdapter(adapter);
    }

    private void loadReminders()
    {
        RemindersDataSource datasource = new RemindersDataSource(this);
        datasource.open();
        reminders = datasource.getReminders();
        if(reminders == null)
        {
            reminders = new ArrayList<Reminder>();
        }
//        Toast toast = Toast.makeText(this, reminders.size() + "", Toast.LENGTH_SHORT);
//        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_new_reminder:
                Intent intent = new Intent(this, NewReminderActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadReminders();
        adapter.clear();
        adapter.addAll(reminders);
        adapter.notifyDataSetChanged();
    }
}
