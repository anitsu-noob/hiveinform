package com.example.hiveinform.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private String id ;
    private String groupId ;
    private String content ;
    private long userId ;
    private LocalDateTime timestamp ;
    private boolean status ;
    private String replay ;
    private long atUser ;
}
