package com.io.mountblue.blogapplication.controller;

import com.io.mountblue.blogapplication.entity.Comment;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.User;
import com.io.mountblue.blogapplication.service.CommentService;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class CommentController {

    PostService postService;
    CommentService commentService;
    UserService userService;

    @Autowired
    public CommentController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/post/comment/{postId}")
    public String writeComment(@PathVariable("postId")int id, Model model,
                               @AuthenticationPrincipal UserDetails userDetails){
        Post post = postService.findPostById(id);
        Integer presentCommentId = 0;
        model.addAttribute("currentUser", userDetails.getUsername());
        model.addAttribute("post", post);
        model.addAttribute("presentCommentId", presentCommentId);
        return "comment";
    }

    @PostMapping("/savecomment")
    public String saveComment(@RequestParam("postId")int id,
                              @ModelAttribute("post")Post post, @RequestParam("commentText")String comment,
                              @ModelAttribute("presentCommentId") int presentCommentId,
                              @AuthenticationPrincipal UserDetails userDetails){
        User author = userService.findUserByName(userDetails.getUsername());
        commentService.saveComment(author, presentCommentId, post, comment, id);

        return "redirect:/post/" + id;
    }

    @GetMapping("/deletecomment/{commentId}/{commentPostId}")
    public String deleteComment(@PathVariable("commentId") int commentId,@PathVariable("commentPostId")int postId){
        commentService.deleteCommentById(commentId);

        return "redirect:/post/"+postId;
    }

    @GetMapping("/updatecomment/{commentId}/{commentPostId}")
    public String updateComment(@PathVariable("commentId") int commentId,@PathVariable("commentPostId")int postId,Model model){
        Comment comment = commentService.findCommentById(commentId);
        comment.setUpdatedAt(String.valueOf(LocalDateTime.now()));
        Post post = postService.findPostById(postId);

        model.addAttribute("post", post);
        model.addAttribute("commentName", comment.getComment());
        model.addAttribute("presentCommentId", commentId);

        return "comment";
    }

}
