package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import com.subham.io.blogapp.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class PostController {
    private PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("/")
    public String showLandingPage(Model model) {
        List<Post> posts = postService.fetchAllPost();
        model.addAttribute("posts", posts);
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
        List<Comment> comments = post.getComment();
        model.addAttribute("comments",comments);
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
}
