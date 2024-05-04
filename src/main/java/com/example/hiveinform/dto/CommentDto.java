package com.example.hiveinform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    @NotBlank
    private String content;
    @NotNull
    private Long userId;

    private Boolean status;
    @Past
    private LocalDateTime releaseTime ;

    private long visited;

    private long likes;

    private long dislikes ;

    private String articleId ;

    private String replay ;

}
