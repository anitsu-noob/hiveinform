package com.example.hiveinform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "articles")
public class Article {

    @Id
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String author;
    @NotBlank
    private String type ;
    @NotBlank
    private Long userId ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate authorCreatedTime;
    private Long visits ;
    private Boolean state;
    private String imageId;
    private String[] images ;
}