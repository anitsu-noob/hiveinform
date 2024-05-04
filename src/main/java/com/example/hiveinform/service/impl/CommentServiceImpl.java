package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.CommentDto;
import com.example.hiveinform.entity.ArticleAndCom;
import com.example.hiveinform.entity.Comment;
import com.example.hiveinform.repository.ArticleAndComRepository;
import com.example.hiveinform.repository.CommentRepository;
import com.example.hiveinform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository ;

    @Autowired
    private ArticleAndComRepository articleAndComRepository ;
    @Override
    public List<Comment> getComments(List<String> ids) {
        List<Comment> comments = commentRepository.findAllById(ids);
        for (Comment comment : comments) {
            if(!comment.getStatus())
                comments.remove(comment);
        }
        //剔除状态异常的 因为是即时性处理方式所以必须得提前剔除 后续的 东西 就得自己查咯
        return comments;
    }

    @Override
    public Comment deleteComment(String id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        comment.setStatus(Boolean.FALSE);
        return comment;
    }

    @Override
    public Comment addComment(Comment comment , String articleId) {
        ArticleAndCom articleAndCom = articleAndComRepository.findArticleAndComByArticleId(articleId);
        if(articleAndCom == null) {
            return new Comment();
        }
        Comment save = commentRepository.save(comment);
        List<String> commentId = articleAndCom.getCommentId();
        commentId.add(save.getId());
        articleAndCom.setCommentId(commentId);
        articleAndComRepository.save(articleAndCom);
        return save;
    }

    @Override
    public List<Comment> getCommentsByUserId(long userId) {
        return commentRepository.getCommentByUserId(userId);
    }

    @Override
    public Comment updateComment(Comment comment) {
        if (commentRepository.findById(comment.getId())!=null) {
            return commentRepository.save(comment);
        }
        return new Comment();
    }
}
