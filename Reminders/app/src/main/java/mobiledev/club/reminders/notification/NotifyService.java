package mobiledev.club.reminders.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import mobiledev.club.reminders.R;
import mobiledev.club.reminders.activities.MainActivity;

/**
 * This service is started when an Alarm has been raised
 *
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell, modified by Jacob Stimes
 */
public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";

    // Name of the intent extra to get the reminder's name
    public static final String INTENT_NAME = "stimes.enterprises.myreminder.remind.INTENT_NAME";

    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification(intent.getStringExtra(INTENT_NAME));

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification(String name) {
        CharSequence title = "Reminder";
        int icon = R.drawable.ic_launcher;

        //This number will be unique from other reminders created because
        // we always increment it below..
        SharedPreferences sharedPref = getSharedPreferences("mobiledev.club.reminders.notificationId", Context.MODE_PRIVATE);
        int uniqueId = sharedPref.getInt("mobiledev.club.reminders.notificationId.id", 1);

        Intent thePendingIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, thePendingIntent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(name)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[] { 1000, 4000 })
                .setLights(Color.RED, 2000, 10000)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(contentIntent);

        // Send the notification to the system.
        mNM.notify(uniqueId, builder.build());


        //After sending the notification, we want to update
        // the saved notification id # so making another
        // reminder will be distinct from this one
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("mobiledev.club.reminders.notificationId.id", uniqueId+1);
        editor.commit();

        // Stop the service when finished
        stopSelf();
    }
}

