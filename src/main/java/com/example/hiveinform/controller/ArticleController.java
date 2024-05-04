package com.example.hiveinform.controller;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.Article;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.service.ArticleService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserInformService userInformService ;

    @Autowired
    private UserService userService;

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/create")
    public Article createArticle(@RequestBody ArticleDto articleDto) {

        Article article = new Article();
        article.setAuthor(articleDto.getAuthor());
        article.setContent(articleDto.getContent());
        article.setState(false);
        article.setTitle(articleDto.getTitle());
        article.setType(articleDto.getType());
        article.setUserId(userService
                .getByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getId());
        Date date = new Date();
        article.setAuthorCreatedTime(articleDto.getAuthorCreatedTime());
        article.setVisits(0L);
        article.setImageId(articleDto.getImageId());
        article.setImages(articleDto.getImages());
        return articleService.createArticle(article);
    }

    @GetMapping("/getArticleByAnonymous")
    public List<Article> getLists()
    {
        ArticleDto articleDto = new ArticleDto() ;
        articleDto.setType("test");
        articleDto.setSize(20);
        articleDto.setPage(0);
        return  articleService.getArticleByType(articleDto);
    }

    @PostMapping("/getArticle/all")
    public List<Article> getArticle(@RequestBody ArticleDto articleDto) {
        return articleService.getArticleByDto(articleDto);
    }

    @GetMapping("/getArticle/{id}")
    public Article getArticleById(@PathVariable String id)
    {
        return articleService.getArticleById(id);
    }

    @GetMapping("/getArticle/foreignId/{id}")
    public Article getArticleById(@PathVariable String id , @RequestBody ArticleDto articleDto)
    {
        if (userService.getUserById(articleDto.getUserId())!=null)
        {
            UserInformDto userInformDto = new UserInformDto();
            userInformDto.setUserId(articleDto.getUserId());
            userInformDto.setArticleId(userInformService.findInform(articleDto.getUserId()).getArticleId());
            userInformDto.setArticleAdd(id);
            userInformService.updateInform(userInformDto);
        }
        return articleService.getArticleById(id);  // 点开某个Article
    }

    @PostMapping("/getArticle/type")
    public List<Article> getArticleByType(@RequestBody ArticleDto articleDto)
    {
        return articleService.getArticleByType(articleDto);
    }

    @PostMapping("/getArticle/content")
    public List<Article> getArticleByContent(@RequestBody ArticleDto articleDto)
    {
        return articleService.getArticleByContent(articleDto);
    }

    @PostMapping("/getArticle/author")
    public List<Article> getArticleByAuthor(@RequestBody ArticleDto articleDto)
    {
        return articleService.getArticleByAuthor(articleDto);
    }

     @GetMapping("/getArticle/title")
    public List<Article> getArticleByTitle(@RequestBody ArticleDto articleDto)
    {
        return articleService.getArticleByTitle(articleDto);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/del")
    public Article deleteArticle(@RequestBody ArticleDto article)
    {
        return articleService.deleteArticle(article.getId());
    }

    @GetMapping("/getByUserId/{userId}")
    public  List<Article> getArticleByUserId(@PathVariable long userId)
    {
        return articleService.getByUserId(userId);
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/getListArticles")
    public List<Article> uploadArticle(@RequestBody String[] articles)
    {



        List<Article> articleList = new ArrayList<Article>();
        for (String article:articles)
        {
            Article articleById = articleService.getArticleById(article);
            if (articleById != null)
                articleList.add(articleById);
        }
        return articleList;
    }

    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/getAdminDisplay")
    public List<Article> display()
    {
        return articleService.displayAdmin();
    }

    //   have fun :)
}
