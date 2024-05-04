package com.example.hiveinform.entity;

import lombok.Data;

@Data
public class EmailSend {
    private String content ;
    private String toAddress ;
    private String from ;
    private String subject ;
}
