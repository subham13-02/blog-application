package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import com.subham.io.blogapp.entity.User;
import com.subham.io.blogapp.service.CommentService;
import com.subham.io.blogapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

@Controller
public class CommentController{
    private CommentService commentService;
    private PostService postService;
    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }
    @GetMapping("/commentpost{postId}")
    public String commentPost(@PathVariable("postId") int postId, Model model) {
        System.out.println(" show comment of id : "+postId);
        Post post=postService.fetchPostById(postId);
        model.addAttribute("post",post);
        Comment newComment=new Comment();
        model.addAttribute("newComment",newComment);
        model.addAttribute("postId",postId);
        return"comment-page";
    }
    @PostMapping("/addcomment{postId}")
    public String addcomment(@PathVariable("postId") int postId, @ModelAttribute("newComment") Comment newComment) {
        System.out.println("id : "+postId);
        commentService.addComment(postId, newComment);
        return "redirect:/commentpost"+postId;
    }

    @GetMapping("/editcomment{commentId}")
    public String editComment(@PathVariable("commentId") int commentId) {
        System.out.println(" edit comment of id : "+commentId);
        commentService.editCommentById(commentId);

        return "commentpost";
    }
    @GetMapping("/deletecomment{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId) {
        System.out.println(" delete comment of id : "+commentId);
        int postId=commentService.getPostIdByCommentId(commentId);
        commentService.deleteCommentById(commentId);
        return "redirect:/commentpost"+postId;
    }
}
