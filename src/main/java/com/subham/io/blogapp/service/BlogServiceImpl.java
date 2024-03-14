package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostsRepository;
import com.subham.io.blogapp.entity.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {
    private PostsRepository postsRepository;

    @Autowired
    public BlogServiceImpl(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    public void save(Posts posts) {
        postsRepository.save(posts);
    }
}
