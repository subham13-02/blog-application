package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.CommentRepository;
import com.io.mountblue.blogapplication.entity.Comment;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void saveComment(User user, int presentCommentId, Post post, String commentString, int postId) {
        if(presentCommentId == 0) {
            Comment theComment = new Comment(user.getName(), user.getEmail(), commentString);
            post.addComment(theComment);
            theComment.setPostId(postId);
            theComment.setCreatedAt(String.valueOf(LocalDateTime.now()));
            theComment.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            commentRepository.save(theComment);
        }else{
            Comment comment1 = findCommentById(presentCommentId);
            comment1.setComment(commentString);
            comment1.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            commentRepository.save(comment1);
        }
    }

    @Override
    public void deleteCommentById(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment findCommentById(int id) {
        Optional<Comment> result = commentRepository.findById(id);
        Comment comment = null;
        if(result.isPresent()){
            comment = result.get();
        }
        else {
            throw new RuntimeException("Did not find comment id - " + id);
        }
        return comment;
    }
}
