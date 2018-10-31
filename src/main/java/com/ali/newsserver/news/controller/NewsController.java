package com.ali.newsserver.news.controller;

import com.ali.newsserver.news.exception.ResourceNotFoundException;
import com.ali.newsserver.news.model.News;
import com.ali.newsserver.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/news")
    public Page<News> getQuestions(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @PostMapping("/news")
    public News createNews(@Valid @RequestBody News news) {
        return newsRepository.save(news);
    }

    @PutMapping("/news/{newsId}")
    public News updateNews(@PathVariable Long newsId,
                                   @Valid @RequestBody News newsRequest) {
        return newsRepository.findById(newsId)
                .map(news -> {
                    news.setTitle(newsRequest.getTitle());
                    news.setDescription(newsRequest.getDescription());
                    return newsRepository.save(news);
                }).orElseThrow(() -> new ResourceNotFoundException("news not found with id " + newsId));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<?> deleteNews(@PathVariable Long newsId) {
        return newsRepository.findById(newsId)
                .map(news -> {
                    newsRepository.delete(news);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("news not found with id " + newsId));
    }
}
