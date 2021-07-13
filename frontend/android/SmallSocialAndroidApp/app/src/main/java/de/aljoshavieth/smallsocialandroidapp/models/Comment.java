package de.aljoshavieth.smallsocialandroidapp.models;

public class Comment {
    private String id;
    private String content;
    private User author;
    private long time;

    public Comment(String id, String content, User author, long time) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public long getTime() {
        return time;
    }
}
