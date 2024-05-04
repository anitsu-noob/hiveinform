package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.ArticleDto;
import com.example.hiveinform.entity.Article;
import com.example.hiveinform.entity.ArticleAndCom;
import com.example.hiveinform.repository.ArticleAndComRepository;
import com.example.hiveinform.repository.ArticleRepository;
import com.example.hiveinform.service.ArticleService;
import com.example.hiveinform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("articleService")
public class  ArticleServiceImpl implements ArticleService {
    //模糊查找 all 指定的是 search 值 多值查询

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserService userService ;

    @Autowired
    private ArticleAndComRepository articleAndComRepository ;

    public Article createArticle(Article article) {
        Article save = articleRepository.save(article);
        ArticleAndCom articleAndCom = new ArticleAndCom();
        articleAndCom.setArticleId(save.getId());
        articleAndCom.setCommentId(new ArrayList<String>());
        articleAndComRepository.save(articleAndCom);
        return save;  //上传之前校验格式 上传之后格式不会更改 按内容 存储
    }

    @Override
    public List<Article> getArticleByTitle(ArticleDto articleDto) {
         PageRequest request = getRequest(articleDto);

        return getArticleList(articleRepository.findTitleContaining(articleDto.getTitle(),request));
    }

    @Override
    public List<Article> getArticleByType(ArticleDto articleDto) {
        PageRequest request = getRequest(articleDto) ;
        return getArticleList(articleRepository.findTypeContaining(articleDto.getType(),request));
    }

    @Override
    public List<Article> getArticleByContent(ArticleDto articleDto) {

        PageRequest request = getRequest(articleDto) ;
        return getArticleList(articleRepository.findContentContaining(articleDto.getContent(),request));

    }

    @Override
    public List<Article> getArticleByAuthor(ArticleDto articleDto) {

        PageRequest request = getRequest(articleDto) ;
        return getArticleList(articleRepository.findAuthorContaining(articleDto.getAuthor(), request));

    }

    @Override
    public Article getArticleById(String id) {
        Article article = articleRepository.findById(id).orElse(new Article());
        return article;
    }

    @Override
    public List<Article> getArticleByDto(ArticleDto articleDto) {
        PageRequest request = getRequest(articleDto) ;
        return getArticleList( articleRepository.findTitleContainingAndTypeContainingAndAuthorContainingAndContentContaining( articleDto.getSearch(), request));

                                                                                                                                                                    //模糊查找
        // 注意： 按人气排序后的只要满足条件就会输出 输出第多少页 和 输出多少个每页都是 前端给的参数
    }

    @Override
    public Article deleteArticle(String id) {
        Article article = articleRepository.findById(id).orElse(null);

        if (article != null) {
            article.setState(false);
            articleRepository.save(article);
            return article;
        }
        else {
            throw new IllegalStateException("非法注入");
        }
    }

    @Override
    public Article updateArticle(Article article) {
           return articleRepository.save(article);
    }

    @Override
    public List<Article> getByUserId(long userId) {
        List<Article> articlesByUserId = articleRepository.findArticlesByUserId(userId);

        if (articlesByUserId.size() > 0)
        {
            return articlesByUserId;
        }
        return new ArrayList<>();
    }

    @Override
    public List<Article> displayAdmin() {
        return articleRepository.findArticlesByState(false);
    }


    public PageRequest getRequest(ArticleDto articleDto)
    {
        return  PageRequest.of(articleDto.getPage(),articleDto.getSize(),Sort.by(Sort.Direction.DESC,"status","visits"));
    }

    public List<Article> getArticleList(Page<Article> article)
    {
        List<Article> articles = new ArrayList<Article>();
        long id = -1;
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            id = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        for (Article article1 : article) {
            if (article1.getState() || article1.getUserId() == id)   // 剔除 异常文章 但是如果是自己的 可以显示
                articles.add(article1);
        }
        return articles;
    }

}
