package com.owl.crazynote;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by owl on 19.05.16.
 */
public class Task implements Serializable, Comparable<Task> {
    private String task, date;
    private int id;

    public Task(String task, String date) {
        this.task = task;
        this.date = date;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Task{" +
                "task='" + task + '\'' +
                ", date='" + date + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(Task another) {
        return ((Integer) getId()).compareTo(another.getId());
    }
}