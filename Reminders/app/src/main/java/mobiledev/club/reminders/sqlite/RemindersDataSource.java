package mobiledev.club.reminders.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mobiledev.club.reminders.models.Reminder;

/**
 * Created by Ethan on 2/23/2015.
 */
public class RemindersDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ReminderSQLiteHelper dbHelper;
    private String[] allColumns = { ReminderSQLiteHelper.COLUMN_ID,
            ReminderSQLiteHelper.COLUMN_TITLE, ReminderSQLiteHelper.COLUMN_DESCRIPTION, ReminderSQLiteHelper.COLUMN_DATE };

    public RemindersDataSource(Context context) {
        dbHelper = new ReminderSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createReminder(Reminder reminder) {
        String title = reminder.getTitle();
        String description = reminder.getDescription();
        Date date = reminder.getDueDate();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String dateString = dateFormat.format(date);

        ContentValues values = new ContentValues();
        values.put(ReminderSQLiteHelper.COLUMN_TITLE, title);
        values.put(ReminderSQLiteHelper.COLUMN_DESCRIPTION, description);
        values.put(ReminderSQLiteHelper.COLUMN_DATE, description);
        database.insert(ReminderSQLiteHelper.TABLE_REMINDERS, null,
                values);
        /*
        Cursor cursor = database.query(ReminderSQLiteHelper.TABLE_REMINDERS,
                allColumns, ReminderSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;*/
    }

    public void deleteReminder(Reminder reminder) {
        long id = reminder.getId();
        //System.out.println("Comment deleted with id: " + id);
        database.delete(ReminderSQLiteHelper.TABLE_REMINDERS, ReminderSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Reminder> getReminders() {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();

        Cursor cursor = database.query(ReminderSQLiteHelper.TABLE_REMINDERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Reminder reminder = cursorToReminder(cursor);
            reminders.add(reminder);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return reminders;
    }

    private Reminder cursorToReminder(Cursor cursor) {
        Reminder reminder = new Reminder();
        reminder.setID(cursor.getLong(0));
        reminder.setTitle(cursor.getString(1));
        reminder.setDescription(cursor.getString(2));
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        try{
            reminder.setDueDate(dateFormat.parse(cursor.getString(3)));
        } catch (ParseException e)
        {
            reminder.setDueDate(new Date());
        }

        return reminder;
    }

}
