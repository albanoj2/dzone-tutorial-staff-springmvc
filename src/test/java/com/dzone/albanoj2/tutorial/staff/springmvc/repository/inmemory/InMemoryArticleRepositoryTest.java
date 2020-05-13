package com.dzone.albanoj2.tutorial.staff.springmvc.repository.inmemory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dzone.albanoj2.tutorial.staff.springmvc.domain.Article;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleNotFoundException;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleSaveRequest;

public class InMemoryArticleRepositoryTest {

    private InMemoryArticleRepository repository;
    
    @BeforeEach
    public void setUp() {
        repository = new InMemoryArticleRepository();
    }
    
    @Test
    public void givenNoExistingArticles_whenFindAll_thenNoArticlesFound() {
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    public void givenOneArticleExists_whenFindAll_thenArticleFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        repository.create(request);
        
        List<Article> found = repository.findAll();
        
        assertEquals(1, found.size());
        assertEquivalentFound(found, request);
    }
    
    private void assertEquivalentFound(List<Article> articles, ArticleSaveRequest request) {
        
        Optional<Article> found = articles.stream()
            .filter(article -> 
                Objects.equals(request.getTitle(), article.getTitle()) &&
                Objects.equals(request.getAuthor(), article.getAuthor())
            )
            .findAny();
        
        assertTrue(found.isPresent());
    }
    
    @Test
    public void givenTwoArticlesExist_whenFindAll_thenBothArticleFound() {
        
        ArticleSaveRequest request1 = new ArticleSaveRequest("foo1", "bar1");
        ArticleSaveRequest request2 = new ArticleSaveRequest("foo2", "bar2");
        
        repository.create(request1);
        repository.create(request2);
        
        List<Article> found = repository.findAll();
        
        assertEquals(2, found.size());
        assertEquivalentFound(found, request1);
        assertEquivalentFound(found, request2);
    }
    
    @Test
    public void givenNoExistingArticles_whenFindById_thenNoArticleFound() {
        assertFalse(repository.findById("foo").isPresent());
    }
    
    @Test
    public void givenOneExistingArticleWithDifferentId_whenFindById_thenNoArticleFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        Article created = repository.create(request);
        
        assertFalse(repository.findById(created.getId() + "blah").isPresent());
    }
    
    @Test
    public void givenOneExistingArticle_whenFindById_thenArticleFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        Article created = repository.create(request);
        
        Optional<Article> found = repository.findById(created.getId());
        
        assertTrue(found.isPresent());
        assertIdPopulated(found.get().getId());
        assertEquals(request.getTitle(), found.get().getTitle());
        assertEquals(request.getAuthor(), found.get().getAuthor());
    }

    private static void assertIdPopulated(String id) {
        assertNotNull(id);
        assertFalse(id.isBlank());
    }
    
    @Test
    public void givenNoArticlesExist_whenUpdate_thenArticleNotFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        assertThrows(ArticleNotFoundException.class, () -> {
            repository.update("baz", request);
        });
    }
    
    @Test
    public void givenOneArticleExistsWithDifferentId_whenUpdate_thenArticleNotFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        Article existing = repository.create(request);
        
        assertThrows(ArticleNotFoundException.class, () -> {
            repository.update(existing.getId() + "blah", request);
        });
    }
    
    @Test
    public void givenOneArticleExists_whenUpdate_thenArticleUpdated() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("newTitle", "newAuthor");
        
        Article existing = repository.create(request);
        
        Article updated = repository.update(existing.getId(), request);
        
        assertEquals(updated.getId(), existing.getId());
        assertEquals(updated.getTitle(), existing.getTitle());
        assertEquals(updated.getAuthor(), existing.getAuthor());
    }
    
    @Test
    public void givenNoArticlesExist_whenDelete_thenArticleNotFound() {
        
        assertThrows(ArticleNotFoundException.class, () -> {
            repository.delete("baz");
        });
    }
    
    @Test
    public void givenOneArticleExistsWithDifferentId_whenDelete_thenArticleNotFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        Article existing = repository.create(request);
        
        assertThrows(ArticleNotFoundException.class, () -> {
            repository.delete(existing.getId() + "blah");
        });
    }
    
    @Test
    public void givenOneArticleExists_whenDelete_thenArticleNotFound() {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        Article existing = repository.create(request);
        
        repository.delete(existing.getId());
        
        assertTrue(repository.findById(existing.getId()).isEmpty());
        assertTrue(repository.findAll().isEmpty());
    }
}
