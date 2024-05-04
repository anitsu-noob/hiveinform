package com.example.hiveinform.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
@Data
public class ArticleAndCom {
    @Id
    private String id ;
    private String articleId ;
    @Field("comment_id")
    private List<String> commentId ;
}
