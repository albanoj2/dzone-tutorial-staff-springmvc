package com.dzone.albanoj2.tutorial.staff.springmvc.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dzone.albanoj2.tutorial.staff.springmvc.domain.Article;

@Component
public class ArticleResourceAssembler {

    public List<ArticleResource> toResources(List<Article> articles) {
        return articles.stream()
            .map(this::toResource)
            .collect(Collectors.toList());
    }
    
    public ArticleResource toResource(Article article) {
        
        ArticleResource resource = new ArticleResource();
        
        resource.setId(article.getId());
        resource.setTitle(article.getTitle());
        resource.setAuthor(article.getAuthor());
        
        return resource;
    }
}
