package com.example.hiveinform.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Data
public class Comment {

    @Id
    private String id;

    private String content;

    private Long  UserId;

    private Boolean status;

    private LocalDateTime releaseTime ;

    private long visits;

    private long likes;

    private long dislikes ;

    private String replay ;

}
