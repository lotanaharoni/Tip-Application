package com.example.tips;

public class Tip implements Comparable {
    String type, user_uploaded, text, id;
    int counter_likes;

    public Tip(String type, String user_uploaded, String text, int counter_likes, String id) {
        this.type = type;
        this.user_uploaded = user_uploaded;
        this.text = text;
        this.counter_likes = counter_likes;
        this.id = id;
    }

    public Tip() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser_uploaded(String user_uploaded) {
        this.user_uploaded = user_uploaded;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCounter_likes(int counter_likes) {
        this.counter_likes = counter_likes;
    }

    public String getType() {
        return type;
    }

    public String getUser_uploaded() {
        return user_uploaded;
    }

    public String getText() {
        return text;
    }

    public int getCounter_likes() {
        return counter_likes;
    }

    @Override
    public int compareTo(Object o) {
        return ((Tip)o).counter_likes - this.counter_likes;
    }
}
