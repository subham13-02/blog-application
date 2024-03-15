package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.CommentRepository;
import com.subham.io.blogapp.dao.PostRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    Date date = new Date();
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
    public void addComment(int postId, Comment newComment) {
        Post post = postService.fetchPostById(postId);
        List<Comment> comments = post.getComment();
        newComment.setName(post.getAuthorId().getName());
        newComment.setEmail(post.getAuthorId().getEmail());
        newComment.setUpdatedAt(date);
        newComment.setCreatedAt(date);
        comments.add(newComment);
        post.setComment(comments);
        postRepository.save(post);
    }
    @Override
    public void editCommentById(int commentId) {
//        commentRepository.

    }
    @Override
    public void deleteCommentById(int commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public int getPostIdByCommentId(int commentId) {
        System.out.println(commentId);
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


}

