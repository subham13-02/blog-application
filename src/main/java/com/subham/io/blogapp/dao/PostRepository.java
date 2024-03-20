package com.subham.io.blogapp.dao;

import com.subham.io.blogapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContaining(String title, String content, String authorName, String tagName);
    List<Post> findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContainingOrderByPublishedAtDesc(String title, String content, String authorName, String tagName);
    Set<Post> findAllByTagsNameIn(Set<String> tagNames);
    Set<Post> findAllByAuthorIdNameIn(Set<String> authors);
}
