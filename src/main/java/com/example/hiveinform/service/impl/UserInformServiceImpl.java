package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.Article;
import com.example.hiveinform.entity.Image;
import com.example.hiveinform.entity.UserInform;
import com.example.hiveinform.repository.ArticleRepository;
import com.example.hiveinform.repository.ImageRepository;
import com.example.hiveinform.repository.UserInformRepository;
import com.example.hiveinform.repository.UserRepository;
import com.example.hiveinform.service.UserInformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userInform")
public class UserInformServiceImpl implements UserInformService {

    @Autowired
    private UserInformRepository userInformRepository ;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private ImageRepository imageRepository ;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public UserInform addInform(UserInformDto userInformDto) {
        if (userInformRepository.getUserInformByUserId(userInformDto.getUserId())!=null)
        {
            throw new RuntimeException("非法的注入") ;
        }
        if (userRepository.findById(userInformDto.getUserId()).orElse(null)!=null)
        {
            UserInform userInform = new UserInform();
            userInform.setUserId(userInformDto.getUserId());
            userInform.setArticleId(userInformDto.getArticleId());
            return userInformRepository.save(userInform) ;
        }
        else throw new RuntimeException("非法的注入") ;
    }

    @Override
    public UserInform updateInform(UserInformDto userInformDto) {
        if (userInformRepository.getUserInformByUserId(userInformDto.getUserId())==null)
        {
            throw new RuntimeException("非法的注入") ;
        }
        UserInform userInform = userInformRepository.getUserInformByUserId(userInformDto.getUserId());
        List<String> articleId = userInform.getArticleId();
        if (articleId.indexOf(userInformDto.getArticleAdd()) == -1 && userInformDto.getArticleAdd() != null)
        {
           Article article = articleRepository.findById(userInformDto.getArticleAdd()).orElse(null);
            if (article != null && article.getState())
            {
                articleId.add(userInformDto.getArticleAdd());
                article.setVisits(article.getVisits() + 1);
                articleRepository.save(article);
            }
        }
        else
        {
            if (articleId.remove(userInformDto.getArticleAdd()))
            articleId.add(userInformDto.getArticleAdd());
        }

        userInform.setArticleId(articleId);
        return userInformRepository.save(userInform) ;
    }

    @Override
    public UserInform findInform(long userId) {
        return userInformRepository.getUserInformByUserId(userId);
    }

    @Override
    public UserInform registerSetInform(UserInformDto userInformDto) {
        long id = userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        UserInform userInform = userInformRepository.getUserInformByUserId(id);
        userInform.setImageId(userInformDto.getImageId());
        userInform.setUserName(userInformDto.getUserName());
        return userInformRepository.save(userInform);
    }

    /**
     * this function is from the securityHolderContext get the login flag and role inform to get the UserInform
     *
     * @param
     *
     * @return userinform
     *
     * **/
    @Override
    public UserInform getInform() {
        long id = userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return userInformRepository.getUserInformByUserId(id);
    }
}
