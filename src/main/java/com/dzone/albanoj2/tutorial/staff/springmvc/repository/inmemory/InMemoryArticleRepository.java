package com.dzone.albanoj2.tutorial.staff.springmvc.repository.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.dzone.albanoj2.tutorial.staff.springmvc.domain.Article;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleNotFoundException;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleRepository;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleSaveRequest;

@Repository
public class InMemoryArticleRepository implements ArticleRepository {
    
    private final List<Article> articles;
    
    public InMemoryArticleRepository() {
        articles = new ArrayList<>();
    }

    @Override
    public Optional<Article> findById(String id) {
        return articles.stream()
           .filter(article -> article.getId().equals(id))
           .findAny();
    }

    @Override
    public List<Article> findAll() {
        return articles;
    }

    @Override
    public Article create(ArticleSaveRequest request) {

        Article article = new Article(generateId(), request.getTitle(), request.getAuthor());
        
        articles.add(article);
        
        return article;
    }
    
    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Article update(String id, ArticleSaveRequest request) {
        
        Article existing = findById(id).orElseThrow(ArticleNotFoundException::new);
        
        existing.setTitle(request.getTitle());
        existing.setAuthor(request.getAuthor());
        
        return existing;
    }

    @Override
    public void delete(String id) {
        Article existing = findById(id).orElseThrow(ArticleNotFoundException::new);
        articles.remove(existing);
    }

    @Override
    public void deleteAll() {
        articles.clear();
    }
}
