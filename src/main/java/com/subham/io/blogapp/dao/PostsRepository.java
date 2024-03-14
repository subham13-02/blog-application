package com.subham.io.blogapp.dao;

import com.subham.io.blogapp.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts,Integer> {
//    public List<Posts> findAll();
}
