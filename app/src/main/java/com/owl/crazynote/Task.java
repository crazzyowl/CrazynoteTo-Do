package com.owl.crazynote;

import java.io.Serializable;

/**
 * Created by owl on 19.05.16.
 */
public class Task implements Serializable{
    private String task, date;

    public Task(String task, String date) {
        this.task = task;
        this.date = date;
    }

    public Task() {
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
