package com.example.hiveinform.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "image")
public class Image {
    @Id
    private String id;

    private String filename;

    private String contentType;

    private long size;

    private long userId;

}