package com.example.hiveinform.service.impl;

import com.example.hiveinform.service.UserService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.example.hiveinform.entity.Image;
import com.example.hiveinform.repository.ImageRepository;
import com.example.hiveinform.service.ImageService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ImageRepository imageRepository ;

    @Autowired
    private GridFSBucket gridFSBucket ;

    @Autowired
    private UserService userService ;
    @Override
    public String addImage(MultipartFile file) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("filename", file.getOriginalFilename());
        metaData.put("contentType", file.getContentType());
        metaData.put("size", file.getSize());

        Image image = new Image();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename() ,file.getContentType() , metaData);  // 获取当前文件内容
            image.setId(objectId.toString());
            image.setFilename(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setSize(file.getSize());
            long id = userService.getByUsername(authentication.getName()).getId();
            image.setUserId(id);
        }
        else {
            throw new IllegalStateException("User can't be null the authenticated is null");
        }
        imageRepository.insert(image);

        return image.getId();
    }

    @Override
    public String getImage(String imageId) throws IOException {
        GridFSFindIterable gridFSFiles = null;
        Image image = imageRepository.findById(imageId).orElse(new Image());
        if (image != null) {
            Query query = Query.query(Criteria.where("_id").is(image.getId()));
            gridFSFiles = gridFsTemplate.find(query);
            if (gridFSFiles != null) {
                GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFiles.first().getObjectId());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = downloadStream.read(buffer)) != -1) {
                    bytes.write(buffer, 0, bytesRead);
                }
                byte[] byteArray = bytes.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(byteArray);

                // 构建带有 Base64 图片数据的 data URI
                String dataUri = "data:" + image.getContentType() + ";base64," + base64Image;

                return dataUri;
            }
        }
        return null;
    }

    @Override
    public Image removeImage(String imageId) {
        Image image = imageRepository.findById(imageId).orElse(new Image());

        if (image != null) {
            imageRepository.delete(image);
            Query query = Query.query(Criteria.where("_id").is(image.getId()));
            gridFsTemplate.delete(query);
        }
        return image;
    }

    @Override
    public List<Image> getImageByUserIdAll(long userId) throws IOException {
        return imageRepository.getImageByUserId(userId);
    }

    @Override
    public Image getImageByUserId(long userId) throws IOException {
        List<Image> imageByUserId = imageRepository.getImageByUserId(userId);
        if (imageByUserId.size() > 0)
        return imageByUserId.get(imageByUserId.size()-1);
        else return null ;
    }

    @Override
    public String createImage(MultipartFile file,long id) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("filename", file.getOriginalFilename());
        metaData.put("contentType", file.getContentType());
        metaData.put("size", file.getSize());

        ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename() ,file.getContentType() , metaData);  // 获取当前文件内容
        Image image = new Image();
        image.setId(objectId.toString());
        image.setFilename(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setUserId(id);
        imageRepository.insert(image);

        return image.getId();
    }

}
