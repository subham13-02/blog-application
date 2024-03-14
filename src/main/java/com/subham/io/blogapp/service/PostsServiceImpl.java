package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostsRepository;
import com.subham.io.blogapp.entity.Posts;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostsServiceImpl implements PostsService {
    private PostsRepository postsRepository;

    @Autowired
    public PostsServiceImpl(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Override
    public void save(Posts posts) {
        postsRepository.save(posts);
    }

    @Override
    public List<Posts> fetchAllPost() {
        List<Posts> posts=postsRepository.findAll();
        return posts;
    }

    @Override
    public Posts fetchPostById(int id){
        Optional<Posts> result = postsRepository.findById(id);
        Posts post = null;
        if(result.isPresent()){
            post = result.get();
        }else {
            throw new RuntimeException();
        }
        return post;
    }
}
