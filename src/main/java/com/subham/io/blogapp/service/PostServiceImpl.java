package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.PostRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import com.subham.io.blogapp.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserService userService;
    LocalDate date = LocalDate.now();
    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }
    @Override
    public void publish(Post post) {
        User user =userService.getUserByPostId(1);
        post.setAuthorId(user);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setPublishedAt(date);
        post.setPublished(true);

        String content = post.getContent();
        if (content.length() >= 180) {
            post.setExcerpt(content.substring(0, 180));
        } else {
            post.setExcerpt(content);
        }
        postRepository.save(post);
    }

    @Override
    public List<Post> fetchAllPost() {
        List<Post> posts= postRepository.findAll();
        return posts;
    }
    @Override
    public Post fetchPostById(int postId){
        Optional<Post> result = postRepository.findById(postId);
        Post post = null;
        if(result.isPresent()){
            post = result.get();
        }else {
            throw new RuntimeException();
        }
        return post;
    }
    @Override
    public void updateById(int id, Post post) {
        Optional<Post> result = postRepository.findById(id);
        Post existingPost = null;
        if(result.isPresent()){
            existingPost = result.get();
        }else {
            throw new RuntimeException("No post found with Id: "+id);
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setTags(post.getTags());
        existingPost.setContent(post.getContent());
        String content = post.getContent();
        if (content.length() >= 180) {
            existingPost.setExcerpt(content.substring(0, 180));
        } else {
            existingPost.setExcerpt(content);
        }
        postRepository.save(existingPost);
    }
    @Override
    public void deletePostById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> search(String searchQuery,String sortBy) {
        List<Post> searchResults;
//        if(sortBy.equals("newest")){
//            searchResults = postRepository.findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContainingOrderByPublishedAtDesc(searchQuery, searchQuery, searchQuery, searchQuery);
//        }else if(sortBy.equals("title")){
//            searchResults = postRepository.findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContaining(searchQuery, searchQuery, searchQuery, searchQuery);
//        } else{
//            searchResults = postRepository.findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContaining(searchQuery, searchQuery, searchQuery, searchQuery);
//        }
        return null;
    }

    @Override
    public List<Post> search(String searchQuery, String sortBy, Set<String> selectedTags, Set<String> selectedAuthor, String startingDate, String endingDate) {
        List<Post> searchResults;
        Set<Post> filterResult=new HashSet<>();
        List<Post> finalResults=new ArrayList<>();

        if(sortBy.equals("newest")){
            searchResults = postRepository.findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContainingOrderByPublishedAtDesc(searchQuery, searchQuery, searchQuery, searchQuery);
        }else if(sortBy.equals("oldest")){
            searchResults = postRepository.findByTitleContainingOrContentContainingOrAuthorIdNameContainingOrTagsNameContaining(searchQuery, searchQuery, searchQuery, searchQuery);
        }
        else{
            searchResults=postRepository.findAll();
        }

        if (selectedAuthor!= null) {
            Set<Post> authorsResult = postRepository.findAllByAuthorIdNameIn(selectedAuthor);
            if(!authorsResult.isEmpty()){
                filterResult.addAll(authorsResult);
            }
        }
        if (selectedTags!= null) {
            Set<Post> tagsResult = postRepository.findAllByTagsNameIn(selectedTags);
            if(tagsResult!= null){
                filterResult.addAll(tagsResult);
            }
        }
//        if (!(endingDate.isEmpty() || startingDate.isEmpty())) {
//            if (!startingDate.isEmpty()) {
//                startingDate = "0000-00-00";
//            }
//            if (!endingDate.isEmpty()) {
//                endingDate = "9999-99-99";
//            }
//            Set<Post> dateResult = postRepository.findAllByPublishedAtBetween(startingDate, endingDate);
//            if(!dateResult.isEmpty()){
//                filterResult.addAll(dateResult);
//            }
//        }
        for (Post post : searchResults) {
            System.out.println(post);
            if (filterResult.contains(post)) {
                finalResults.add(post);
            }
        }
        if(filterResult.isEmpty()){
            finalResults = searchResults;
        }
//        for (Post post : filterResult){
//            System.out.println("filter Result---------->"+post);
//        }
//        for (Post post : searchResults){
//            System.out.println("Search Result---------->"+post);
//        }
//        for (Post post : finalResults){
//            System.out.println("final Result---------->"+post);
//        }
        return finalResults;
    }
}
