package com.example.hiveinform.repository;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.entity.Article;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    @Query(value = "{$or: [{'title': {$regex: ?0, $options: 'i'}}, {'type': {$regex: ?0, $options: 'i'}},{'content': {$regex: ?0, $options: 'i'}} ,{ 'author': {$regex: ?0, $options: 'i'}} ]} ")
    Page<Article> findTitleContainingAndTypeContainingAndAuthorContainingAndContentContaining( String search, Pageable pageable);    //模糊查找
    @Query("{'type': {$regex: ?0}}")
    Page<Article> findTypeContaining(String type, Pageable pageable );
    @Query("{'title': {$regex: ?0}}")
    Page<Article> findTitleContaining(String title, Pageable pageable ) ;
    @Query("{'author': {$regex: ?0}}")
    Page<Article> findAuthorContaining(String author, Pageable pageable ) ;
    @Query("{'content': {$regex: ?0}}")
    Page<Article> findContentContaining(String content, Pageable pageable ) ;

    List<Article> findArticlesByUserId(long userId);

    List<Article> findArticlesByState(Boolean state);

}
