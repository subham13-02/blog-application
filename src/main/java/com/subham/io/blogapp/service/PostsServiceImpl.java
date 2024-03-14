package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostsRepository;
import com.subham.io.blogapp.entity.Posts;
import com.subham.io.blogapp.entity.User;
import jakarta.transaction.Transactional;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostsServiceImpl implements PostsService {
    private PostsRepository postsRepository;
    Date date = new Date();
    @Autowired
    public PostsServiceImpl(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }
    @Override
    public void save(Posts post) {
        User user = new User("sub", "sub@gmail.com", "sub");
        post.setAuthorId(user);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setPublishedAt(date);
        post.setPublished(true);

        String content = post.getContent();
        if (content.length() > 150) {
            post.setExcerpt(content.substring(0, 150));
        } else {
            post.setExcerpt(content);
        }
        postsRepository.save(post);
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

    @Override
    public void updateById(int id,Posts post) {
        Optional<Posts> result = postsRepository.findById(id);
        Posts existingPost = null;
        if(result.isPresent()){
            existingPost = result.get();
        }else {
            throw new RuntimeException("No post found with Id: "+id);
        }
        // Modify the fields of the retrieved post with the new values
        existingPost.setTitle(post.getTitle());
        existingPost.setTags(post.getTags());
        existingPost.setContent(post.getContent());
        existingPost.setPublished(post.isPublished());

        // Save the modified post back to the database
        postsRepository.save(existingPost);
    }
    @Override
    public void deletePostById(int id) {
        postsRepository.deleteById(id);
    }
}
