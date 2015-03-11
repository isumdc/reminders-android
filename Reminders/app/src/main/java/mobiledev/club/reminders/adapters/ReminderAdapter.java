package mobiledev.club.reminders.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.models.Reminder;
import mobiledev.club.reminders.sqlite.RemindersDataSource;

/**
 * Created by Ethan on 2/5/2015.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList;

    public ReminderAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }


    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder reminderViewHolder, int i) {
        Reminder reminder = reminderList.get(i);
        String title = reminder.getTitle();
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.US);
        String date = dateFormat.format(reminder.getDueDate());
        String description = reminder.getDescription();
        reminderViewHolder.vTitle.setText(title);
        reminderViewHolder.vDate.setText(date);
        reminderViewHolder.vDescription.setText(description);
    }


    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_reminder, viewGroup, false);

        ReminderAdapter.ReminderViewHolder vh = new ReminderViewHolder(itemView, new ReminderAdapter.ReminderViewHolder.MyViewHolderClickListener(){
            public void expandReminder(View view, int position, LinearLayout expandableLayout)
            {
                if(expandableLayout.getVisibility() == View.VISIBLE)
                {
                    expandableLayout.setVisibility(View.GONE);
                }
                else
                {
                    expandableLayout.setVisibility(View.VISIBLE);
                }
            }
            public void deleteReminder(View view, int position)
            {
                Reminder reminder = reminderList.get(position);
                reminderList.remove(position);
                notifyItemRemoved(position);
                RemindersDataSource datasource = new RemindersDataSource(view.getContext());
                datasource.open();
                datasource.deleteReminder(reminder);
                Toast toast = Toast.makeText(view.getContext(), "Reminder successfully deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        return vh;
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MyViewHolderClickListener mListener;
        protected TextView vTitle;
        protected TextView vDate;
        protected TextView vDescription;
        protected Button vDeleteButton;
        protected LinearLayout vExpandableLayout;

        public ReminderViewHolder(View v, MyViewHolderClickListener listener) {
            super(v);
            mListener = listener;
            vTitle =  (TextView) v.findViewById(R.id.textview_name);
            vDate = (TextView)  v.findViewById(R.id.textview_date);
            vDescription = (TextView) v.findViewById(R.id.textview_description);
            vDeleteButton = (Button) v.findViewById(R.id.button_delete);
            vExpandableLayout = (LinearLayout) v.findViewById(R.id.linearLayout_expandable);
            v.setOnClickListener(this);
            vDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.deleteReminder(v, getPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            mListener.expandReminder(v, position, vExpandableLayout);

        }

        public static interface MyViewHolderClickListener {
            public void expandReminder(View view, int position, LinearLayout expandableLayout);
            public void deleteReminder(View view, int position);
        }
    }
}
