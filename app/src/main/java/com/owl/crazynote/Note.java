package com.owl.crazynote;

import java.io.Serializable;

/**
 * Created by owl on 19.05.16.
 */
public class Note implements Serializable, Comparable<Note> {
    private String title, date, description;
    private int id, colorCircleIcon;

    public Note(String title, String date, String description, int colorCircleIcon) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.colorCircleIcon = colorCircleIcon;
    }

    public int getColorCircleIcon() {
        return colorCircleIcon;
    }

    public void setColorCircleIcon(int colorCircleIcon) {
        this.colorCircleIcon = colorCircleIcon;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(Note another) {
        return ((Integer) getId()).compareTo(another.getId());
    }
}