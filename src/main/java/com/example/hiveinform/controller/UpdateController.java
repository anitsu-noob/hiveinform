package com.example.hiveinform.controller;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.entity.Article;
import com.example.hiveinform.service.ArticleService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInformService userInformService ;

    @Autowired
    private UserService userService;


    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/update")
    public Article updateArticle(@RequestBody ArticleDto articleDto)
    {
        if (userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId() == articleDto.getUserId())
        {
            Article save = articleService.getArticleById(articleDto.getId());
            if ( save!= null) {
                save.setState(articleDto.getState());
                save.setType(articleDto.getType());
                save.setContent(articleDto.getContent());
                save.setTitle(articleDto.getTitle());
                save.setAuthor(articleDto.getAuthor());
                save.setVisits(articleDto.getVisits());
                save.setImageId(articleDto.getImageId());
                save.setImages(articleDto.getImages());
                save.setAuthorCreatedTime(articleDto.getAuthorCreatedTime());
            }
            return articleService.updateArticle(save);
        }
        throw new IllegalStateException("你不能修改他人的文章");
    }



    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/updateByAdmin/{id}/{state}")
    public Article updateArticleByAdmin(@PathVariable String id, @PathVariable Boolean state)
    {
        Article save = articleService.getArticleById(id);
        if ( save!= null) {
            save.setState(state);
            return articleService.updateArticle(save);
        }
        throw new IllegalStateException("haven't find article by id " + id);

    }

}
