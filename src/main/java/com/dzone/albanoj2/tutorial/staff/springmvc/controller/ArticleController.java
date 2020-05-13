package com.dzone.albanoj2.tutorial.staff.springmvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dzone.albanoj2.tutorial.staff.springmvc.domain.Article;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleNotFoundException;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleRepository;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleSaveRequest;
import com.dzone.albanoj2.tutorial.staff.springmvc.resource.ArticleResource;
import com.dzone.albanoj2.tutorial.staff.springmvc.resource.ArticleResourceAssembler;

@Controller
@RequestMapping("/article")
public class ArticleController {
    
    @Autowired
    private ArticleRepository repository;
    
    @Autowired
    private ArticleResourceAssembler assembler;

    @GetMapping
    public ResponseEntity<List<ArticleResource>> findAll() {
        
        List<Article> articles = repository.findAll();
        
        return new ResponseEntity<>(assembler.toResources(articles), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResource> findById(@PathVariable String id) {
        return repository.findById(id)
            .map(article -> new ResponseEntity<>(assembler.toResource(article), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<ArticleResource> create(@RequestBody ArticleSaveRequest request) {
        
        Article created = repository.create(request);
        
        return new ResponseEntity<>(assembler.toResource(created), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResource> update(@PathVariable String id, @RequestBody ArticleSaveRequest request) {
        
        try {
            Article updated = repository.update(id, request);
            return new ResponseEntity<>(assembler.toResource(updated), HttpStatus.OK);
        }
        catch (ArticleNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResource> delete(@PathVariable String id) {
        
        try {
            repository.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (ArticleNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
