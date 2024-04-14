package com.io.mountblue.blogapplication.dao;

import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.Tag;
import com.io.mountblue.blogapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {
@Query("SELECT DISTINCT p FROM Post p " +
            "JOIN FETCH p.tags t " +
            "WHERE " +
            "(:field IS NULL OR p.author.name LIKE %:field% OR " +
            "p.title LIKE %:field% OR " +
            "p.content LIKE %:field% OR " +
            "t.name LIKE  %:field%) AND " +
            "(:authors IS NULL OR p.author.name IN :authors) AND " +
            "(:tagNames IS NULL OR t.name IN :tagNames) AND " +
            "((:startDate IS NULL OR :endDate IS NULL) OR p.publishedAt BETWEEN :startDate AND :endDate)")
    Page<Post> findAllBySearchWithFilters(String field, Set<String> authors, Set<String> tagNames, String startDate, String endDate,Pageable pageable);
}
