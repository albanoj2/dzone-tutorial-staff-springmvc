package com.dzone.albanoj2.tutorial.staff.springmvc.repository;

import java.util.List;
import java.util.Optional;

import com.dzone.albanoj2.tutorial.staff.springmvc.domain.Article;

public interface ArticleRepository {

    public Optional<Article> findById(String id);
    public List<Article> findAll();
    public Article create(ArticleSaveRequest request);
    public Article update(String id, ArticleSaveRequest request);
    public void delete(String id);
    public void deleteAll();
}
