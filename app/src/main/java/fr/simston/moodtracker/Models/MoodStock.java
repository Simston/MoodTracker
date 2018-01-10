package fr.simston.moodtracker.Models;

import java.util.Date;

/**
 * Created by St&eacute;phane Simon on 06/01/2018.
 *
 * @version 1.0
 */

public class MoodStock {

    private int day;
    private int month;
    private int positionOfMood;
    private Date date;
    private String commentMessage;

    public MoodStock(int day, int month, int positionOfMood, Date date, String commentMessage) {
        this.day = day;
        this.month = month;
        this.positionOfMood = positionOfMood;
        this.date = date;
        this.commentMessage = commentMessage;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getPositionOfMood() {
        return positionOfMood;
    }

    public void setPositionOfMood(int positionOfMood) {
        this.positionOfMood = positionOfMood;
    }


}
