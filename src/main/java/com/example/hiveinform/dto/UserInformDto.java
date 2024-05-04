package com.example.hiveinform.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserInformDto {
    private long userId ;
    private String userName ;
    private List<String> articleId ;
    private String articleAdd ;
    private String imageId ;
    private Map<String,Integer> GroupReadLine ;
    private List<String> GroupJoined ;
    private List<String> GroupVisited ;
}
