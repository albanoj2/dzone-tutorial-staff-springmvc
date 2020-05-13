package com.dzone.albanoj2.tutorial.staff.springmvc.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.dzone.albanoj2.tutorial.staff.springmvc.domain.Article;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleRepository;
import com.dzone.albanoj2.tutorial.staff.springmvc.repository.ArticleSaveRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {
    
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ArticleRepository repository;
    
    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void givenNoExistingArticles_whenFindAll_thenNoArticlesFound() throws Exception {
        mockMvc.perform(get("/article"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void givenOneArticleExists_whenFindAll_thenExistingArticleFound() throws Exception {
        
        Article article = givenArticleExistsWith("foo", "bar");
        
        mockMvc.perform(get("/article"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(article.getId()))
            .andExpect(jsonPath("$[0].title").value(article.getTitle()))
            .andExpect(jsonPath("$[0].author").value(article.getAuthor()));
    }
    
    private Article givenArticleExistsWith(String title, String author) {
        
        ArticleSaveRequest request = new ArticleSaveRequest(title, author);
        
        return repository.create(request);
    }

    @Test
    public void givenNoExistingArticles_whenFindById_thenNoArticleFound() throws Exception {
        mockMvc.perform(get("/article/foo"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenOneArticleExists_whenFindById_thenArticleFound() throws Exception {
        
        Article article = givenArticleExistsWith("foo", "bar");
        
        mockMvc.perform(get("/article/" + article.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(article.getId()))
            .andExpect(jsonPath("$.title").value(article.getTitle()))
            .andExpect(jsonPath("$.author").value(article.getAuthor()));
    }

    @Test
    public void givenOneArticleExists_whenFindByIdWithDifferentId_thenArticleNotFound() throws Exception {
        
        Article article = givenArticleExistsWith("foo", "bar");
        
        mockMvc.perform(get("/article/" + article.getId() + "blah"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void givenNoExistingArticles_whenCreate_thenArticleCreated() throws Exception {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(request))
             )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value(request.getTitle()))
            .andExpect(jsonPath("$.author").value(request.getAuthor()));
        
        assertCreated(request);
    }
    
    private void assertCreated(ArticleSaveRequest request) {
        
        Optional<Article> found = repository.findAll()
            .stream()
            .filter(article -> 
                Objects.equals(request.getTitle(), article.getTitle()) &&
                Objects.equals(request.getAuthor(), article.getAuthor())
            )
            .findAny();
        
        assertTrue(found.isPresent());
    }
    
    @Test
    public void givenNoRequestBody_whenCreate_thenInvalidRequest() throws Exception {
        mockMvc.perform(post("/article"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenOneArticleExistsWithSameTitleAndAuthor_whenCreate_thenArticleCreated() throws Exception {
        
        ArticleSaveRequest request = new ArticleSaveRequest("foo", "bar");
        
        givenArticleExistsWith(request.getTitle(), request.getAuthor());
        
        mockMvc.perform(post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(request))
             )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value(request.getTitle()))
            .andExpect(jsonPath("$.author").value(request.getAuthor()));
        
        assertCreated(request);
    }
    
    @Test
    public void givenNoRequestBody_whenUpdate_thenInvalidRequest() throws Exception {
        mockMvc.perform(put("/article/foo"))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenNoExistingArticles_whenUpdate_thenArtilcleNotFound() throws Exception {
        
        ArticleSaveRequest request = new ArticleSaveRequest("newTitle", "newAuthor");
        
        mockMvc.perform(put("/article/foo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenOneArticleExists_whenUpdate_thenArticleUpdated() throws Exception {
        
        ArticleSaveRequest request = new ArticleSaveRequest("newTitle", "newAuthor");
        
        Article article = givenArticleExistsWith("foo", "bar");
        
        mockMvc.perform(put("/article/" + article.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(article.getId()))
            .andExpect(jsonPath("$.title").value(request.getTitle()))
            .andExpect(jsonPath("$.author").value(request.getAuthor()));
    }
    
    @Test
    public void givenNoExistingArticles_whenDelete_thenArtilcleNotFound() throws Exception {
        mockMvc.perform(delete("/article/foo"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenOneArticleExists_whenDelete_thenArticleDeleted() throws Exception {
        
        Article article = givenArticleExistsWith("foo", "bar");
        
        mockMvc.perform(delete("/article/" + article.getId()))
            .andExpect(status().isNoContent());
    }
}
