package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import com.subham.io.blogapp.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserService userService;
    Date date = new Date();
    @Autowired
    public PostServiceImpl(PostRepository postRepository , UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }
    @Override
    public void publish(Post post) {
        User user =userService.getUserByPostId(1);
        post.setAuthorId(user);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setPublishedAt(date);
        post.setPublished(true);

        String content = post.getContent();
        if (content.length() >= 180) {
            post.setExcerpt(content.substring(0, 180));
        } else {
            post.setExcerpt(content);
        }
        postRepository.save(post);
    }

    @Override
    public List<Post> fetchAllPost() {
        List<Post> posts= postRepository.findAll();
        return posts;
    }
    @Override
    public Post fetchPostById(int postId){
        Optional<Post> result = postRepository.findById(postId);
        Post post = null;
        if(result.isPresent()){
            post = result.get();
        }else {
            throw new RuntimeException();
        }
        return post;
    }
    @Override
    public void updateById(int id, Post post) {
        Optional<Post> result = postRepository.findById(id);
        Post existingPost = null;
        if(result.isPresent()){
            existingPost = result.get();
        }else {
            throw new RuntimeException("No post found with Id: "+id);
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setTags(post.getTags());
        existingPost.setContent(post.getContent());
        String content = post.getContent();
        if (content.length() >= 180) {
            existingPost.setExcerpt(content.substring(0, 180));
        } else {
            existingPost.setExcerpt(content);
        }
        postRepository.save(existingPost);
    }
    @Override
    public void deletePostById(int id) {
        postRepository.deleteById(id);
    }
}
