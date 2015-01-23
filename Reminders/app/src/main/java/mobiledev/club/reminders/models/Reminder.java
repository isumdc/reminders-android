package mobiledev.club.reminders.models;

import java.util.Date;

/**
 * Created by Ethan on 1/23/2015.
 */
public class Reminder {

    private String name;
    private String description;
    private Date dueDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Reminder()
    {

    }

}
