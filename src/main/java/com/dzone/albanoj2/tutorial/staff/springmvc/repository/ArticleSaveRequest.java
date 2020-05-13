package com.dzone.albanoj2.tutorial.staff.springmvc.repository;

public class ArticleSaveRequest {

    private String title;
    private String author;
    
    public ArticleSaveRequest(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    public ArticleSaveRequest() {}

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
