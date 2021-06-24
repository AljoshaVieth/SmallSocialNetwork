package de.aljoshavieth.smallsocialandroidapp;

import java.util.ArrayList;

public class Post {
    private String id;
    private String content;
    private String color;
    private ArrayList<Comment> comments;
    private User author;
    private long time;

    public Post(String id, String content, String color, ArrayList<Comment> comments, User author, long time) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.comments = comments;
        this.author = author;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public User getAuthor() {
        return author;
    }

    public long getTime() {
        return time;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

}
