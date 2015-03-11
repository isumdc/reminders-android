package mobiledev.club.reminders.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by Ethan on 3/11/2015.
 */
public class NewReminderDialog extends DialogFragment {

    private Dialog dialog;
    private Activity activity;
    private View view;
    private NewReminderDialogListener mCallback;

    private Button dateButton;
    private Button timeButton;
    private Button saveButton;
    private Button cancelButton;

    private static final String dialogTitle = "New Reminder";
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

    public interface NewReminderDialogListener {
        void addNewReminder(Reminder reminder);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        activity = getActivity();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = getActivity().getLayoutInflater().inflate(
                R.layout.dialog_new_reminder, null);

        builder.setView(view);
        // Create the AlertDialog object and return it
        dialog = builder.create();

        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(activity);
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

        dateButton = (Button) view.findViewById(R.id.button_datepicker);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateButtonOnClick(v);
            }
        });

        timeButton = (Button) view.findViewById(R.id.button_timepicker);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeButtonOnClick(v);
            }
        });

        saveButton = (Button) view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonOnClick(v);
            }
        });

        cancelButton = (Button) view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        updateDateText();
        updateTimeText();

        //dialog.setTitle(dialogTitle);


        //Button saveButton = (Button) view.findViewById(R.id.button_save);
        return dialog;
    }

    private void saveReminder(Reminder reminder) {

        RemindersDataSource datasource = new RemindersDataSource(activity);
        datasource.open();
        datasource.createReminder(reminder);
        datasource.close();

        long timeInMs = reminder.getDueDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMs);

        //The schedule client handles setting up when the reminder goes off
        scheduleClient.setAlarmForNotification(reminder.getTitle(), calendar);

        //Makes toast:
        Toast.makeText(getActivity(), "Reminder set for " + SimpleDateFormat.getDateTimeInstance().format(new Date(timeInMs)), Toast.LENGTH_LONG).show();
        //activity.addReminder(reminder);
    }

    public void saveButtonOnClick(View v) {
        /*
        Toast toast = Toast.makeText(NewReminderActivity.this, "Button Pressed", Toast.LENGTH_LONG);
        toast.show();*/

        EditText nameTextView = (EditText) view.findViewById(R.id.edittext_name);
        String name = nameTextView.getText().toString();

        EditText descriptionTextView = (EditText) view.findViewById(R.id.edittext_description);
        String description = descriptionTextView.getText().toString();

        if (name.isEmpty() || description.isEmpty()) {
            Toast toast = Toast.makeText(view.getContext(), "All fields must be filled out", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Reminder newReminder = new Reminder();

        newReminder.setTitle(name);
        newReminder.setDescription(description);
        newReminder.setDueDate(dueDate);

        //Save to database

        saveReminder(newReminder);
        addNewReminder(newReminder);
        dialog.cancel();

        //NewReminderActivity.this.finish();
    }

    public void dateButtonOnClick(View view) {
        DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance(Locale.US);
                cal.set(year, monthOfYear, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                dueDate = cal.getTime();
                updateDateText();
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    public void timeButtonOnClick(View view) {
        TimePickerDialog dialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                Calendar cal = Calendar.getInstance(Locale.US);
                cal.set(mYear, mMonth, mDay, hour, minute);
                mHour = hour;
                mMinute = minute;
                dueDate = cal.getTime();
                updateTimeText();
            }
        }, mHour, mMinute, false);
        dialog.show();
    }

    private void updateDateText() {
        String dueDateString = dateFormat.format(dueDate);
        TextView dateTextView = (TextView) view.findViewById(R.id.textview_date);
        dateTextView.setText(datePrefix + dueDateString);
    }

    private void updateTimeText() {
        String dueTimeString = timeFormat.format(dueDate);
        TextView timeTextView = (TextView) view.findViewById(R.id.textview_time);
        timeTextView.setText(timePrefix + dueTimeString);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (NewReminderDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTimePickedListener.");
        }
    }

    public void addNewReminder(Reminder reminder)
    {
        if(mCallback != null)
        {
            mCallback.addNewReminder(reminder);
        }
    }

    /**
     * Overridden so we can unbind the schedule client
     */
    @Override
    public void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        //  This stops us leaking our activity into the system
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}
