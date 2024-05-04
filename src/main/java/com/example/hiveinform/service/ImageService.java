package com.example.hiveinform.service;

import com.example.hiveinform.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    String addImage(MultipartFile file) throws IOException;

    String getImage(String imageId) throws IOException;

    Image removeImage(String imageId);

    List<Image> getImageByUserIdAll (long userId) throws IOException;

    Image getImageByUserId(long userId) throws IOException;

    String createImage(MultipartFile file,long id) throws IOException;

}
