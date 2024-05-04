package com.example.hiveinform.controller;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.dto.TypeDto;
import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.Article;
import com.example.hiveinform.entity.Type;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.entity.UserInform;
import com.example.hiveinform.service.*;
import com.example.hiveinform.util.RabbitMQManager;
import jakarta.persistence.AssociationOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/index")
public class HomePageController {
    private final int page_num = 10 ;
    @Autowired
    private TypeService typeService ;

    @Autowired
    private UserService userService ;

    @Autowired
    private RedisService redisService ;

    @Autowired
    private UserInformService userInformService ;

    @Autowired
    private ArticleService articleService ;

    @Autowired
    private RabbitMQManager rabbitMQManager;

    @PostMapping("/type")
    public void getType(@RequestBody TypeDto typeDto)
    {

        if(typeDto == null)
        {
            return;
        }
        typeService.createType(typeDto);
    }

    @GetMapping("/isAdmin")
    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public Boolean isAdmin()
    {
        return true;
    }

    @GetMapping("/getArticle/{id}")
    public List<Article> getArticlesByType(@PathVariable long id)
    {
        UserInform inform = userInformService.findInform(id);
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
        {
            if (inform.getUserId() != id)
            {
                throw new IllegalStateException ("you get the type not yourself");
            }
        }
        List<String> articleId = inform.getArticleId();

        int i = articleId.size() - page_num - 1;
        if(i < 0)
        {
            i=0 ;
        }
        HashMap<Object, Integer> types = new HashMap<Object, Integer>();
        String typeOut = "" ;
        boolean flag = false ;
        int max = 1;
        for (;i< articleId.size()-1;i++)
        {
            String type = articleService.getArticleById(articleId.get(i)).getType();
            if (flag == false)
            {
                typeOut = type ;
                flag = true ;
            }
            if (types.get(type)==null)
            types.put(type, 1);
            else
            {
                int num = types.get(type);
                types.replace(type,num++);
                if(num > max)
                {
                    max = num ;
                    typeOut = type ;
                }
            }

        }

        ArticleDto articleDto = new ArticleDto() ;
        articleDto.setType(typeOut);
        articleDto.setSize(20);
        articleDto.setPage(0);
        TypeDto typeDto = new TypeDto();
        typeDto.setHobbies(typeOut);
        typeDto.setId(inform.getUserId());
        typeService.createType(typeDto);
        return articleService.getArticleByType(articleDto);

    }   //主动获取最近喜欢看的 类型文章 用作推荐



    @PostMapping("/logout")
    public void logout() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")){
            redisService.delete(authentication.getName());
            SecurityContextHolder.clearContext();
        }
    }

    @PostMapping("/getEmail")
    public String getEmail() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")){
            User user = userService.getByUsername(authentication.getName());
            return user.getEmail();
        }
        throw new IllegalStateException("请登录");
    }

}
