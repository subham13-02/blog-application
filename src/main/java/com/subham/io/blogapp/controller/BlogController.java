package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Posts;
import com.subham.io.blogapp.entity.User;
import com.subham.io.blogapp.service.BlogService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BlogController{
    private BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

//    @RequestMapping("/")
//    public String showLandingPage(Model theModel){
//        return "landing-page";
//    }
    @GetMapping("/newpost")
    public String showForm(Model theModel){
        Posts posts = new Posts();
        theModel.addAttribute("post",posts);
        return "post-form";
    }
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post") Posts posts){
        System.out.println("saved");
        User user=new User("subham","sahu13@gmail.com","1234");
        posts.setAuthorId(user);
        System.out.println(posts.getTitle());
        blogService.save(posts);
        return "redirect:/newpost";
    }
//    @GetMapping("/showFormForUpdate")
//    public  String showFormForUpdate(@RequestParam("employeeId") int theId, Model theModel){
//        Employee theEmployee = employeeService.findById(theId);
//
//        theModel.addAttribute("employee",theEmployee);
//        return "employees/employee-form";
//    }
}
