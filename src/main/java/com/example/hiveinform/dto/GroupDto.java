package com.example.hiveinform.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    private String id ;
    private List<Long> userId ;
    private List<Long> adminId ;
    private boolean status ;
    private String groupName ;
    private long level ;
    private long createUser ;
    private String description ;
    private String imageId ;
    private long belongsUser ;
    private long heat ;
    private int page ;
    private int size ;
}
