package com.subham.io.blogapp.service;

import com.subham.io.blogapp.entity.Comment;

public interface CommentService {
    void addComment(int postId, Comment newComment);

    void deleteCommentById(int commentId);

    int getPostIdByCommentId(int commentId);

    String getCommentByCommentId(int commentId);

    void updateComment(int postId, Comment comment);
}
