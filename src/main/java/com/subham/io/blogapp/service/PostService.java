package com.subham.io.blogapp.service;

import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;

import java.util.List;
import java.util.Set;

public interface PostService {
    void publish(Post post);
    List<Post> fetchAllPost();
    Post fetchPostById(int postId);
    void updateById(int postId, Post post);
    void deletePostById(int postId);
    List<Post> search(String searchQuery, String sortBy);
    List<Post> search(String searchQuery, String sortBy, Set<String> selectedTags, Set<String> selectedAuthor, String startingDate, String endingDate);
}
