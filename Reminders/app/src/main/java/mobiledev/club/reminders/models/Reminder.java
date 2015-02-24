package mobiledev.club.reminders.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ethan on 1/23/2015.
 */
public class Reminder implements Serializable{

    private long id;
    private String title;
    private String description;
    private Date dueDate;

    public Reminder()
    {

    }

    public long getId()
    {
        return id;
    }

    public void setID(long id)
    {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

}
