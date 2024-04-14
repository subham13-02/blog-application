package com.io.mountblue.blogapplication.service;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;


public interface PostService {
    void publish(Post post, List<Tag> postTagList, UserDetails userDetails, int presentPostId);

    List<Post> findAllPosts();

    Post findPostById(int id);

    void deletePost(Post post);

    Page<Post> getPostsBySearchWithFilter(String field, Set<String> authors, Set<String> tagNames, String startDate, String endDate, int pageNo, String sortDir);

    Page<Post> getPaginatedPosts(int pageNo, int pageSize);

    boolean isUserValidToOperate(UserDetails userDetails, int postId);
}
