package com.dzone.albanoj2.tutorial.staff.springmvc.domain;

public class Article {

    private String id;
    private String title;
    private String author;
    
    public Article(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    
    public Article() {}

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
}
