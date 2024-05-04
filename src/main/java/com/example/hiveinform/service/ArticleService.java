package com.example.hiveinform.service;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.entity.Article;

import java.util.List;

public interface ArticleService {
    Article createArticle(Article article);

    List<Article> getArticleByTitle(ArticleDto articleDto);

    List<Article> getArticleByType(ArticleDto articleDto);

    List<Article> getArticleByContent(ArticleDto articleDto);

    List<Article> getArticleByAuthor(ArticleDto articleDto);

    Article getArticleById(String id);

    List<Article> getArticleByDto(ArticleDto articleDto);

    Article deleteArticle(String id);

    Article updateArticle(Article article);

    List<Article> getByUserId(long userId);

    List<Article> displayAdmin();

}
