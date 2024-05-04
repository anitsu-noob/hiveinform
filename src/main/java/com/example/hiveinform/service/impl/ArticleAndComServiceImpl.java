package com.example.hiveinform.service.impl;

import com.example.hiveinform.entity.ArticleAndCom;
import com.example.hiveinform.repository.ArticleAndComRepository;
import com.example.hiveinform.repository.ArticleRepository;
import com.example.hiveinform.service.ArticleAndComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("articleAndComService")
public class ArticleAndComServiceImpl implements ArticleAndComService {

    @Autowired
    private ArticleAndComRepository articleAndComRepository ;

    @Autowired
    private ArticleRepository articleRepository ;

    @Override
    public ArticleAndCom findArticleAndComByArticleId(String articleId) {
        ArticleAndCom articleAndComByArticleId = articleAndComRepository.findArticleAndComByArticleId(articleId);
        if (articleRepository.findById(articleAndComByArticleId.getArticleId()).orElse(null).getState())
        return articleAndComByArticleId;
        else
            return new ArticleAndCom() ;
    }

    @Override
    public ArticleAndCom AddArticleAndCom(ArticleAndCom articleAndCom) {
        return articleAndComRepository.save(articleAndCom);
    }

    @Override
    public ArticleAndCom getArticleAndCom(String id) {
        return articleAndComRepository.findById(id).orElse(new ArticleAndCom());
    }

    @Override
    public ArticleAndCom getArticleAndComByArticleId(String articleId) {
        return articleAndComRepository.findArticleAndComByArticleId(articleId);
    }
}

