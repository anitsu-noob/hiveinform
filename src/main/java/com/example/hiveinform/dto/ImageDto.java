package com.example.hiveinform.dto;

import lombok.Data;

@Data
public class ImageDto {

    private String filename;

    private String contentType;

    private long size;

    private String imageId ;
}
