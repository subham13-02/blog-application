package com.io.mountblue.blogapplication.RestController;

import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.Tag;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class PostRestController {
    PostService postService;
    TagService tagService;
    public PostRestController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> showPosts(){
        List<Post> posts = postService.findAllPosts();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> showSinglePost(@PathVariable("postId")int id){
        Post post = postService.findPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<String> updateExistingPostById(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable("postId") int postId,
                                                         @RequestBody Post post,
                                                         @RequestParam("tags") String tags){
        Post existingPost = postService.findPostById(postId);
        List<Tag> postTagList = tagService.tagHandlerInDb(tags);
        System.out.println(userDetails+"hi");
        if( existingPost == null){
            return new ResponseEntity<>("Invalid Post id",HttpStatus.BAD_REQUEST);
        }

        boolean userAuthorized = postService.isUserValidToOperate(userDetails, postId);

        if(userAuthorized){
            post.setAuthor(existingPost.getAuthor());
            postService.publish(post, postTagList, userDetails, postId);

            return new ResponseEntity<>("Post updated successfully",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/publishform")
    public ResponseEntity<String> publishForm(@RequestBody Post post,
                                              @RequestParam("tags") String tags,
                                              @RequestParam(name = "postId") int presentPostId,
                                              @AuthenticationPrincipal UserDetails userDetails){
        if(postService.isUserValidToOperate(userDetails,presentPostId)){
            List<Tag> postTagList = tagService.tagHandlerInDb(tags);
            postService.publish(post, postTagList, userDetails, presentPostId);
            return new ResponseEntity<>("Post created successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/filter/{pageNo}")
    public ResponseEntity<List<Post>> applyFilter(@RequestParam(required = false) Set<String> tagNames,
                                                  @RequestParam(required = false) Set<String> authorsName, Model model,
                                                  @RequestParam(name="startDate", required = false) String startDate,
                                                  @RequestParam(name="endDate", required = false) String endDate,
                                                  @RequestParam(value = "field",required = false) String field,
                                                  @RequestParam(value = "sortDir",required = false) String sortDir,
                                                  @PathVariable("pageNo") int pageNo){
        Page<Post> page = postService.getPostsBySearchWithFilter(field, authorsName, tagNames, startDate, endDate,pageNo, sortDir);
        List<Post> posts = page.getContent();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deleteExistingPostById(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable("postId") int postId){
        if( postService.findPostById(postId) == null){
            return new ResponseEntity<>("Invalid Post id",HttpStatus.BAD_REQUEST);
        }
        boolean isAuthorized = postService.isUserValidToOperate(userDetails, postId);
        Post post = postService.findPostById(postId);
        if(isAuthorized){
            postService.deletePost(post);
            return new ResponseEntity<>("Post deleted successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Access Denied",HttpStatus.UNAUTHORIZED);
    }
}
