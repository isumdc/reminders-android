package mobiledev.club.reminders.notification;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;


/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 *
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 *
 * This will run on it's own thread.
 *
 * @author paul.blundell, Modified by Jacob Stimes
 */
public class AlarmTask implements Runnable {
    //Name/memo for alarm:
    private final String name;
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;


    public AlarmTask(Context context, String name, Calendar date) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.name = name;
    }

    @Override
    public void run() {
        // Request to start our service when the reminder date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(NotifyService.INTENT_NAME, name);

        //This file contains a field "....notification.id" that
        // will always have a unique notification id # we can
        // use for reminders so they don't get combined. The value is updated
        // in NotifyService
        SharedPreferences sharedPref = context.getSharedPreferences("mobiledev.club.reminders.notificationId", Context.MODE_PRIVATE);
        int uniqueId = sharedPref.getInt("mobiledev.club.reminders.notificationId.id", 1);

        //This is what distinguishes one NotifyService intent from another,
        // to allow for multiple reminders to be set simultaneously
        intent.putExtra("NotifId", uniqueId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
    }
}

