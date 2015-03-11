package mobiledev.club.reminders.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.adapters.ReminderAdapter;
import mobiledev.club.reminders.models.Reminder;
import mobiledev.club.reminders.sqlite.RemindersDataSource;


public class MainActivity extends ActionBarActivity {

    private static ArrayList<Reminder> reminders;
    //private ListView listView;
    //private static ReminderAdapter adapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadReminders();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_reminders);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ReminderAdapter(reminders);
        mRecyclerView.setAdapter(mAdapter);
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
        //mAdapter.notifyDataSetChanged();
        //adapter.addAll(reminders);
       //adapter.notifyDataSetChanged();
    }
}
