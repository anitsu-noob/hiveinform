package com.example.hiveinform.repository;

import com.example.hiveinform.entity.ArticleAndCom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleAndComRepository extends MongoRepository<ArticleAndCom,String> {
    ArticleAndCom findArticleAndComByArticleId(String articleId);
}
