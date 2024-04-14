package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.CommentRepository;
import com.io.mountblue.blogapplication.entity.Comment;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface CommentService {
    void saveComment(User user, int presentCommentId, Post post, String commentString, int postId);

    void deleteCommentById(int id);

    Comment findCommentById(int id);
}
