package com.io.mountblue.blogapplication.RestController;

import com.io.mountblue.blogapplication.entity.Comment;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.service.CommentService;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    PostService postService;
    CommentService commentService;
    UserService userService;

    @Autowired
    public CommentRestController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/post/comment/{postId}")
    public ResponseEntity<List<Comment>> writeComment(@PathVariable("postId")int postId){
        Post post = postService.findPostById(postId);

        if(post == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity<>(post.getComments(),HttpStatus.OK);
    }

    @PostMapping("/post/comment/{postId}")
    public ResponseEntity<String> saveComment(@PathVariable("postId") int postId,
                                                 @RequestParam("commentString") String commentString,
                                                 @RequestBody Comment comment){
        Post post = postService.findPostById(postId);
        if( postService.findPostById(postId) == null){
            return new ResponseEntity<>("Invalid Post id",HttpStatus.BAD_REQUEST);
        }
        commentService.saveComment(post.getAuthor(), 0,post,commentString,postId );

        return new ResponseEntity<>("Comment saved successfully", HttpStatus.CREATED);
    }

    @PutMapping("/comment/{commentId}/{commentPostId}")
    public ResponseEntity<String> updateComment(@PathVariable("commentId") int commentId,
                                                @RequestParam("commentString") String commentString,
                                                @AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable("commentPostId")int postId,
                                                @RequestBody Comment comment){
        Post post = postService.findPostById(postId);
        Comment existingComment = commentService.findCommentById(commentId);

        boolean userAuthorized = postService.isUserValidToOperate(userDetails, postId);

        if(post==null || existingComment == null || !userAuthorized){
            return new ResponseEntity<>("Invalid Request",HttpStatus.BAD_REQUEST);
        }

        comment.setId(commentId);
        commentService.saveComment(post.getAuthor(), commentId, post,commentString, postId);

        return new ResponseEntity<>("Comment updated successfully",HttpStatus.OK);
    }

    @GetMapping("/deletecomment/{commentId}/{commentPostId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") int commentId,@PathVariable("commentPostId")int postId, @AuthenticationPrincipal UserDetails userDetails){
        if(postService.findPostById(postId) == null){
            return new ResponseEntity<>("Invalid Post Id",HttpStatus.BAD_REQUEST);
        }

        if(commentService.findCommentById(commentId) == null){
            return new ResponseEntity<>("Invalid Comment Id",HttpStatus.BAD_REQUEST);
        }

        if(postService.isUserValidToOperate(userDetails, postId)){
            commentService.deleteCommentById(commentId);
            return new ResponseEntity<>("Comment deleted successfully",HttpStatus.OK);
        }

        return new ResponseEntity<>("Unauthorized User",HttpStatus.UNAUTHORIZED);
    }

}
