package com.example.howmanyseats;

public class userComment {
    String user_name;
    String date;
    String comment;
    int rate;

    public userComment(String user_name, String date, String comment, int rate){
        this.user_name = user_name;
        this.date = date;
        this.comment =comment;
        this.rate = rate;
    }
    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public int getRate() {
        return rate;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
