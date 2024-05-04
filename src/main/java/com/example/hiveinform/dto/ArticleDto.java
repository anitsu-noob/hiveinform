package com.example.hiveinform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDto {
    private String id ;
    private String title;
    private String content;
    private String author;
    private String type ;
    @JsonProperty(value = "userId")
    private Long userId ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @JsonProperty(value = "authorCreatedTime")
    private LocalDate authorCreatedTime;
    private Long visits ;
    private Boolean state;
    private int page ;
    private int size ;
    private String search ;
    @JsonProperty(value = "imageId")
    private String imageId;
    private String[] images ;
}
