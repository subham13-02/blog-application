package com.subham.io.blogapp.controller;

import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.Post;
import com.subham.io.blogapp.service.CommentService;
import com.subham.io.blogapp.service.PostService;
import com.subham.io.blogapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class CommentController{
    Date date=new Date();
    private CommentService commentService;
    private PostService postService;
    private UserService userService;
    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }
    @GetMapping("/commentpost{postId}")
    public String commentPost(@PathVariable("postId") int postId, Model model) {
        Post post=postService.fetchPostById(postId);
        model.addAttribute("post",post);
        Comment comment = new Comment();
        model.addAttribute("currentComment",comment);
        return"comment-page";
    }
    @PostMapping("/addcomment{postId}")
    public String addComment(@PathVariable("postId") int postId, @ModelAttribute("currentComment") Comment comment){
        System.out.println("ADDING COMMENT ON POSTid : "+postId);
        if(comment.getId()==0){
            commentService.addComment(postId,comment);
        }else{
            commentService.updateComment(postId,comment);
        }
        return "redirect:/commentpost"+postId;
    }
    @GetMapping("/editcomment{commentId}")
    public String editComment(@PathVariable("commentId") int commentId,Model model) {
        System.out.println(" edit comment of id : "+commentId);
        int postId=commentService.getPostIdByCommentId(commentId);

        Comment comment=new Comment();
        String commentText=commentService.getCommentByCommentId(commentId);
        comment.setCommentText(commentText);
        comment.setId(commentId);
        model.addAttribute("currentComment",comment);

        System.out.println(comment);
        Post post=postService.fetchPostById(postId);
        model.addAttribute("post",post);
        model.addAttribute("editComment",true);
        return "comment-page";
    }
    @GetMapping("/deletecomment{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId) {
        System.out.println(" delete comment of id : "+commentId);
        int postId=commentService.getPostIdByCommentId(commentId);
        commentService.deleteCommentById(commentId);
        return "redirect:/commentpost"+postId;
    }
}
