package com.example.hiveinform.controller;


import com.example.hiveinform.dto.CommentDto;
import com.example.hiveinform.entity.ArticleAndCom;
import com.example.hiveinform.entity.Comment;
import com.example.hiveinform.repository.CommentRepository;
import com.example.hiveinform.service.ArticleAndComService;
import com.example.hiveinform.service.CommentService;
import jakarta.validation.Valid;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleAndComService articleAndComService ;

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/add")
    public Comment addComment(@RequestBody CommentDto commentDto)
    {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setStatus(true);
        Date date = new Date();
        comment.setReleaseTime(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        comment.setContent(commentDto.getContent());
        comment.setUserId(commentDto.getUserId());
        comment.setLikes(0);
        comment.setDislikes(0);
        comment.setVisits(0);
        comment.setReplay(commentDto.getReplay());
        return  commentService.addComment(comment, commentDto.getArticleId());
    }

    @GetMapping("/findCommentById/{articleId}")
    public List<Comment> findComment (@PathVariable String articleId)
    {
        return commentService.getComments(
                        articleAndComService.
                        findArticleAndComByArticleId(articleId).
                        getCommentId());
    }   // 双重筛选后的结果

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/update")
    public Comment updateComment (@RequestBody Comment comment)
    {
        return commentService.updateComment(comment) ;
    }

    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @PostMapping("/delete/{id}")
    public Comment deleteComment (@PathVariable  String id)
    {
        return commentService.deleteComment(id);
    }


    @GetMapping("/getCommentById/{articleId}")
    public List<Comment> getComment (@PathVariable String articleId){
        return  commentService.getComments(articleAndComService.getArticleAndComByArticleId(articleId).getCommentId());
    }

    @GetMapping("/getCommentByuserId/{userId}")
    public List<Comment> getCommentByUserId(@PathVariable long userId)
    {
        return  commentService.getCommentsByUserId(userId);
    }

    @GetMapping("/getComment/{id}")
    public List<Comment> getCommentById (@PathVariable String id)
    {
        return commentService.getComments(articleAndComService.getArticleAndCom(id).getCommentId());
    }

    @GetMapping("/getByCommentId/{id}")
    public Comment getByCommentId (@PathVariable String id)
    {
        return commentRepository.findById(id).orElse(new Comment());
    }
}
