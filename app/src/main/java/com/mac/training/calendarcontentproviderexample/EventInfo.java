package com.mac.training.calendarcontentproviderexample;

/**
 * Created by User on 8/30/2016.
 */
public class EventInfo {
    private int id;
    private String location;
    private String title;
    private String description;
    private String dtStart;
    private String dtEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Title: " + title + " Description: " + description + " Location: " + location);
        return sb.toString();
    }

    public String getDtStart() {
        return dtStart;
    }

    public void setDtStart(String dtStart) {
        if (dtStart != null) {
            this.dtStart = dtStart;
        } else {
            this.dtStart = "";
        }
    }

    public String getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(String dtEnd) {
        if (this.dtEnd != null) {
            this.dtEnd = dtEnd;
        } else {
            this.dtEnd = "";
        }
    }
}
