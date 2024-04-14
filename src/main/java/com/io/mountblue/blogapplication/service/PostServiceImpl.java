package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.PostRepository;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.Tag;
import com.io.mountblue.blogapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {
    PostRepository postRepository;
    UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserService userService){
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public void publish(Post post, List<Tag> postTagList, UserDetails userDetails, int presentPostId) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(currentDate);
        StringBuilder excerpt = new StringBuilder();
        if (post.getContent().length() > 151) {
            excerpt.append(post.getContent(), 0, 150);
        } else {
            excerpt.append(post.getContent());
        }

        post.setTags(postTagList);
        User theUser = userService.findUserByName(userDetails.getUsername());
        post.setAuthor(theUser);

        if(presentPostId == 0) {
            post.setPublishedAt(date);
            post.setUpdatedAt(date);
            post.setCreatedAt(date);
        }else{
            post.setUpdatedAt(date);
            post.setPublishedAt(post.getCreatedAt());
        }

        post.setExcerpt(excerpt.toString());
        post.setPublished(true);
        postRepository.save(post);
    }

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post findPostById(int id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = null;

        if (result.isPresent()) {
            post = result.get();
        }
        else {
            throw new RuntimeException("Did not find post id - " + id);
        }
        return post;
    }

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Page<Post> getPostsBySearchWithFilter(String field, Set<String> authors, Set<String> tagNames, String startDate, String endDate, int pageNo, String sortDir) {
        if (startDate != null && startDate.isEmpty()) {
            startDate = null;
        }
        if (endDate != null && endDate.isEmpty()) {
            endDate = null;
        }
        if(sortDir == null ){
            sortDir = "DESC";
        }
        String sortBy = "publishedAt";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1,10, sort);
        return postRepository.findAllBySearchWithFilters(field, authors, tagNames, startDate, endDate, pageable);
    }

    @Override
    public Page<Post> getPaginatedPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return postRepository.findAll(pageable);
    }

    @Override
    public boolean isUserValidToOperate(UserDetails userDetails, int postId) {
        if(postId == 0){
            return true;
        }
        Post post = findPostById(postId);
        if(userDetails == null){
            return false;
        }else if(userDetails.getAuthorities().toString().contains("ROLE_ADMIN")){
            return true;
        } else return userDetails.getUsername().equals(post.getAuthor().getName());
    }
}
