package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Posts;
import com.subham.io.blogapp.entity.User;
import com.subham.io.blogapp.service.PostsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostsController {
    private PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/")
    public String showLandingPage(Model model){
        List<Posts> posts=postsService.fetchAllPost();
        model.addAttribute("posts",posts);
        return "landing-page";
    }
    @GetMapping("/newpost")
    public String showForm(Model theModel){
        Posts posts = new Posts();
        theModel.addAttribute("post",posts);
        return "post-form";
    }
    @PostMapping("/savePost")
    public String savePosts(@ModelAttribute("post") Posts posts){
        System.out.println("saved");
        User user=new User("virat","virat@gmail.com","1234");
        posts.setAuthorId(user);
        postsService.save(posts);
        return "redirect:/newpost";
    }
    @GetMapping("/post{postId}")
    public String showPost(@PathVariable("postId")int id, Model model) {
        System.out.println("inside  show post");
        Posts post = postsService.fetchPostById(id);
        model.addAttribute("post", post);
        return "show-post";
    }
}
