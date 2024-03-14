package com.subham.io.blogapp.service;

import com.subham.io.blogapp.entity.Posts;

import java.util.List;

public interface PostsService {
    void save(Posts posts);
    List<Posts> fetchAllPost();
    Posts fetchPostById(int id);

    void updateById(int id,Posts post);

    void deletePostById(int id);
}
