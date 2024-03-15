package com.subham.io.blogapp.service;

import com.subham.io.blogapp.entity.Comment;

public interface CommentService {
    void addComment(int postId, Comment newComment);


    void editCommentById(int commentId);

    void deleteCommentById(int commentId);

    int getPostIdByCommentId(int commentId);
}
