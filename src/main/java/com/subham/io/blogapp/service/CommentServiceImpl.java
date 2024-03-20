package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.CommentRepository;
import com.subham.io.blogapp.dao.PostRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    LocalDate date = LocalDate.now();
    private PostRepository postRepository;
    private PostService postService;
    private CommentRepository commentRepository;
    @Autowired
    public CommentServiceImpl(PostRepository postRepository, PostService postService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }
    @Override
    public void addComment(int postId, Comment comment) {
        Post post = postService.fetchPostById(postId);
        System.out.println("This is service comment");
        List<Comment> comments = post.getComments();
        comment.setName(post.getAuthorId().getName());
        comment.setEmail(post.getAuthorId().getEmail());
        comment.setUpdatedAt(date);
        comment.setCreatedAt(date);
        comments.add(comment);
        post.setComments(comments);
        System.out.println(comments);
        postRepository.save(post);
    }
    @Override
    public void deleteCommentById(int commentId) {
        commentRepository.deleteById(commentId);
    }
    @Override
    public int getPostIdByCommentId(int commentId) {
        Optional<Comment> result = commentRepository.findById(commentId);
        Comment comment=null;
        if(result.isPresent()){
            comment = result.get();
        }else {
            throw new RuntimeException("Comment id doesn't exist");
        }
        int postId=comment.getPostId();
        return postId;
    }
    @Override
    public String getCommentByCommentId(int commentId) {
        Optional<Comment> result = commentRepository.findById(commentId);
        Comment comment=null;
        if(result.isPresent()){
            comment = result.get();
        }else {
            throw new RuntimeException("Comment id doesn't exist");
        }
        String commentText=comment.getCommentText();
        return commentText;
    }
    public Comment getCommentDataByCommentId(int commentId) {
        Optional<Comment> result = commentRepository.findById(commentId);
        Comment comment=null;
        if(result.isPresent()){
            comment = result.get();
        }else {
            throw new RuntimeException("Comment id doesn't exist");
        }
        return comment;
    }
    @Override
    public void updateComment(int postId, Comment currentComment) {
        Comment comment= getCommentDataByCommentId(currentComment.getId());
        comment.setUpdatedAt(date);
        comment.setCommentText(currentComment.getCommentText());
        System.out.println(comment);
        commentRepository.save(comment);
    }
}

