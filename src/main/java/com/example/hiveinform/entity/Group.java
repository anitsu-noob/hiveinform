package com.example.hiveinform.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "group")
public class Group {
    private String id ;
    private List<Long> userId ;
    private List<Long> adminId ;
    private boolean status ;
    private String groupName ;
    private long level ;
    private long createUser ;
    private String description ;
    private long belongsUser ;
    private long heat ;
    private String imageId ;
}
