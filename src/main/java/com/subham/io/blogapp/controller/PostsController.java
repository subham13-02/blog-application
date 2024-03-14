package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Posts;
import com.subham.io.blogapp.entity.User;
import com.subham.io.blogapp.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class PostsController {
    private PostsService postsService;
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/")
    public String showLandingPage(Model model) {
        List<Posts> posts = postsService.fetchAllPost();
        model.addAttribute("posts", posts);
        return "landing-page";
    }

    @GetMapping("/newpost")
    public String showForm(Model theModel) {
        Posts posts = new Posts();
        theModel.addAttribute("post", posts);
        return "post-form";
    }

    @PostMapping("/savepost")
    public String savePosts(@ModelAttribute("post") Posts post) {
        System.out.println("saved");

        postsService.save(post);
        return "redirect:/";
    }

    @GetMapping("/post{postId}")
    public String showPost(@PathVariable("postId") int id, Model model) {
        Posts post = postsService.fetchPostById(id);
        model.addAttribute("post", post);
        return "show-post";
    }

    @GetMapping("/editpost{postId}")
    public String editPost(@PathVariable("postId") int id, Model model) {
        Posts post = postsService.fetchPostById(id);
        model.addAttribute("post",post);
        return "post-form";
    }
    @PostMapping("/updatepost{postId}")
    public String updatePosts(@PathVariable("postId") int id, @ModelAttribute("post")Posts post) {
        post.setId(id);
        System.out.println("Updated"+ post);
        postsService.updateById(post.getId(), post);
        return "redirect:/";
    }
}
