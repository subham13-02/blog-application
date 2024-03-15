package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    Date date=new Date();
    private PostRepository postRepository;
    private PostService postService;

    public CommentServiceImpl(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }
    @Override
    public void addComment(int postId,Comment newComment) {
        Post post=postService.fetchPostById(postId);
        List<Comment> comments = post.getComment();
        newComment.setName(post.getAuthorId().getName());
        newComment.setEmail(post.getAuthorId().getEmail());
        newComment.setUpdatedAt(date);
        newComment.setCreatedAt(date);
        comments.add(newComment);
        post.setComment(comments);
        postRepository.save(post);
    }
}
