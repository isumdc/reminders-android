package mobiledev.club.reminders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;

/**
 * Created by Ethan on 2/5/2015.
 */
public class ArrayAdapterNewReminder extends ArrayAdapter<Reminder> {

    private LayoutInflater mInflater;
    private Context context;
    private int layoutResourceId;
    private ArrayList<Reminder> reminders;

    public ArrayAdapterNewReminder(Context context, int layoutResourceId, ArrayList<Reminder> reminders)
    {
        super(context, layoutResourceId, reminders);
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.reminders = reminders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(layoutResourceId, null);
        TextView nameTextView = (TextView)row.findViewById(R.id.textview_name);
        TextView dateTextView = (TextView)row.findViewById(R.id.textview_date);

        Reminder reminder = (reminders.get(position));
        nameTextView.setText(reminder.getName());

        Date date = reminder.getDueDate();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dateString = dateFormat.format(date);

        dateTextView.setText(dateString);

        return row;
    }
}
