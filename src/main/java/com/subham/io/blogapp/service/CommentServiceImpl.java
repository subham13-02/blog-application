package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    private PostRepository postRepository;
    private PostService postService;
    @Autowired
    public CommentServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Override
    public void addComment( int postId,Comment comment) {
        Post post=postService.fetchPostById(postId);
        List<Comment> comments = post.getComment();
        comments.add(comment);
        post.setComment(comments);
        postRepository.save(post);
    }
}
