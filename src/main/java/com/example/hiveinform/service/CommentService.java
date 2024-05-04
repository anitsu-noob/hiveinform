package com.example.hiveinform.service;

import com.example.hiveinform.dto.CommentDto;
import com.example.hiveinform.entity.Comment;

import java.util.List;

public interface CommentService {
    public List<Comment> getComments(List<String> ids );

    public Comment deleteComment(String id );

    public Comment addComment(Comment comment, String articleId);

    public List<Comment> getCommentsByUserId(long userId);

    public Comment updateComment(Comment comment);
}
