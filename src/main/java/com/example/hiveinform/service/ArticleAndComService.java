package com.example.hiveinform.service;

import com.example.hiveinform.entity.ArticleAndCom;

public interface ArticleAndComService {
    public ArticleAndCom findArticleAndComByArticleId(String articleId);

    public ArticleAndCom AddArticleAndCom(ArticleAndCom articleAndCom) ;

    public ArticleAndCom getArticleAndCom(String id);

    public ArticleAndCom getArticleAndComByArticleId(String articleId) ;
}
