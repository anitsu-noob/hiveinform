package com.example.hiveinform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSelfMessage {
    private String id ;
    private long userId;
    private List<Message> messages ;
}
