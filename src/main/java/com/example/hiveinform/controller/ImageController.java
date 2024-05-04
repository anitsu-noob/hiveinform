package com.example.hiveinform.controller;

import com.example.hiveinform.dto.ImgRepDto;
import com.example.hiveinform.entity.Image;
import com.example.hiveinform.service.ImageService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService ;

    @PostMapping("/upload")
    public String createImage(@RequestParam("file") MultipartFile file) throws IOException
    {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")
                    || file.getContentType().equals("image/jpeg") && userService.getByUsername(username) != null)
                return imageService.createImage(file, userService.getByUsername(username).getId());
            else
                throw new IllegalArgumentException("file type error");
        }
        throw new IllegalArgumentException("you can't upload img in your not login status ");
    }

    @PostMapping("/")
    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException
    {
        if(file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")|| file.getContentType().equals("image/jpeg"))
            return imageService.addImage(file);
        else
            throw new IllegalArgumentException("file type error") ;
    }

    @GetMapping("/{imageId}")
    public String getImage(@PathVariable String imageId) throws IOException {
//        ImgRepDto imgRepDto = new ImgRepDto(imageService.getImage(imageId));
        if(imageId==null || imageId.equals("") || imageId.equals("null"))
            return "null";
        return imageService.getImage(imageId);
    }

    @GetMapping("/get/{imageId}")
    public String getImage2(@PathVariable String imageId) throws IOException {
//        ImgRepDto imgRepDto = new ImgRepDto(imageService.getImage(imageId));
        if(imageId==null || imageId.equals("") || imageId.equals("null"))
            return "null";
        return "<img src='"+ imageService.getImage(imageId)+"'/>";
    }



    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/del/{imageId}")
    public void deleteImage(@PathVariable String imageId) {
         imageService.removeImage(imageId);
    }


    @GetMapping("/background")
    public String getBackground() throws IOException {
        LocalTime currentTime = LocalTime.now();
        String imageId = "";
        if (currentTime.isAfter(LocalTime.of(5, 0)) && currentTime.isBefore(LocalTime.of(12, 0))) {
            imageId = "6512d4928afc9c19d7a31d3c" ;
        } else if (currentTime.isAfter(LocalTime.of(12, 0)) && currentTime.isBefore(LocalTime.of(18, 0))) {
            imageId = "6512d4f68afc9c19d7a31d40" ;
        } else if (currentTime.isAfter(LocalTime.of(18, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            imageId = "6512d4fd8afc9c19d7a31d44" ;
        } else {
            imageId = "6512d5028afc9c19d7a31d50" ;
        }
        return imageService.getImage(imageId) ;
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("/getUserImage")
    public Image getUserImage() throws IOException {
        long id = 1L ;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            id = userService.getByUsername(authentication.getName()).getId();
        }
        else {
            throw new IllegalArgumentException("the Authentication has the illegal controller") ;
        }
        return imageService.getImageByUserId(id) ;
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("/getUserImageAll")
    public List<Image> getUserImage2() throws IOException {
        long id = 1L ;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()&& !authentication.getName().equals("anonymousUser")) {
            id = userService.getByUsername(authentication.getName()).getId();
        }
        else {
            throw new IllegalArgumentException("the Authentication has the illegal controller") ;
        }
        return imageService.getImageByUserIdAll(id) ;
    }



    //
    // 当你不清醒的时候 你应当适当的扭过头去 看待自己 所经历的风景 一遍遍的记住 一遍遍的错过 .... 当你开始后悔的时候 你就会开始发现自己清醒了 无比的清醒
    // 无数次的数着自己的错误 数着自己后悔的事情 即便过去了十几年的光景 它依旧折射进了你的生活 人生何尝不是自己喝醉后的一场久梦呢？ 不真实的 迷茫的

    // 人生何尝不是一次开始和结束？
    // 从生命开始 到生命结束 我们从未停止过 但人生的目标在哪？ 无人可知 包括上帝 包括死神 或许他们被创造也是人类的目的 而人也从来没有意义
}
