package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import com.subham.io.blogapp.entity.Tag;
import com.subham.io.blogapp.entity.User;
import com.subham.io.blogapp.service.CommentService;
import com.subham.io.blogapp.service.PostService;
import com.subham.io.blogapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PostController {
    private PostService postService;
    private UserService userService;
    private CommentService commentService;

    public PostController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String showLandingPage(Model model) {
        List<Post> posts = postService.fetchAllPost();
        model.addAttribute("posts", posts);
        Set<Tag> tags = new HashSet<>();
        for(Post post :posts){
            if(post.getTags()!=null){
                tags.addAll(post.getTags());
            }
        }
        model.addAttribute("tags", tags);

        List<User> authors = new ArrayList<>();
        for(Post post :posts){
            if(post.getAuthorId()!=null){
                authors.add(post.getAuthorId());
            }
        }
        model.addAttribute("authors", authors);
        return "landing-page";
    }
    @GetMapping("/newpost")
    public String showForm(Model theModel) {
        Post post = new Post();
        theModel.addAttribute("post", post);
        return "post-form";
    }
    @PostMapping("/savepost")
    public String savePosts(@ModelAttribute("post") Post post) {
        System.out.println("saved");
        postService.publish(post);
        return "redirect:/";
    }
    @GetMapping("/post{postId}")
    public String showPost(@PathVariable("postId") int postId, Model model) {
        Post post = postService.fetchPostById(postId);
        model.addAttribute("post", post);
        Comment comment = new Comment();
        model.addAttribute("comment",comment);
        return "show-post";
    }
    @GetMapping("/editpost{postId}")
    public String editPost(@PathVariable("postId") int id, Model model) {
        Post post = postService.fetchPostById(id);
        model.addAttribute("post",post);
        return "post-form";
    }
    @PostMapping("/updatepost{postId}")
    public String updatePosts(@PathVariable("postId") int postId, @ModelAttribute("post") Post post) {
        post.setId(postId);
        System.out.println("Updated"+ post);
        postService.updateById(post.getId(), post);
        return "redirect:/";
    }
    @GetMapping("/deletepost{postId}")
    public String deletePost(@PathVariable("postId") int postId, Model model) {
        System.out.println("Deleted "+postId);
        postService.deletePostById(postId);
        return "redirect:/";
    }
    @PostMapping("/search")
    public String search(@RequestParam(name="searchQuery") String searchQuery, @RequestParam(name="sortBy") String sortBy, Model model) {
        System.out.println("Search :"+searchQuery );
        List<Post> searchResults = postService.search(searchQuery,sortBy);
        model.addAttribute("posts", searchResults);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("sortBy", sortBy);
        return "landing-page";
    }
    @PostMapping("/filter")
    public String applyFilters(@RequestParam(name = "selectedTags") Set<String> selectedTags,
                               @RequestParam(name = "selectedAuthor") List<String> selectedAuthor,
                               @RequestParam(name = "startingDate") String startingDate,
                               @RequestParam(name = "startingDate") String endingDate) {
        if (selectedTags != null) {
            for (String tag : selectedTags) {
                System.out.println("Selected tag: " + tag);
            }
        }
        if (selectedTags != null) {
            for (String author : selectedAuthor) {
                System.out.println("Selected author: " + author);
            }
        }
        System.out.println(startingDate+ " "+ endingDate);
        return "redirect:/";
    }
}
