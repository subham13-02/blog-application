package com.subham.io.blogapp.dao;

import com.subham.io.blogapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContaining(String title, String content, String authorName, String tagName);

}
