package com.io.mountblue.blogapplication.controller;
import com.io.mountblue.blogapplication.entity.Post;
import com.io.mountblue.blogapplication.entity.Tag;
import com.io.mountblue.blogapplication.entity.User;
import com.io.mountblue.blogapplication.service.PostService;
import com.io.mountblue.blogapplication.service.TagService;
import com.io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PostController {
    PostService postService;
    TagService tagService;
    UserService userService;

    @Autowired
    public PostController(PostService postService,TagService tagService,UserService userService){
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showPosts(Model model){
        List<Post> posts = postService.findAllPosts();
        List<Tag> tags = tagService.findAllTags();
        List<User> authors = userService.findAuthors();

        model.addAttribute("authors", authors);
        model.addAttribute("tags",tags);
        model.addAttribute("posts",posts);

        return findPaginated(1,model);
    }

    @GetMapping("/newpost")
    public String showPublishForm(Model model){
        Post post = new Post();
        model.addAttribute("post",post);
        model.addAttribute("presentPostId", 0);

        return "publish-form";
    }

    @PostMapping("/publishform")
    public String publishForm( @ModelAttribute("post") Post post,
                               @RequestParam("tag") String tags,
                               @RequestParam("presentPostId") int presentPostId,
                               @AuthenticationPrincipal UserDetails userDetails){
        List<Tag> postTagList = tagService.tagHandlerInDb(tags);
        postService.publish(post, postTagList, userDetails, presentPostId);

        return "redirect:/";
    }

    @GetMapping("/post/{postId}")
    public String showSinglePost(@PathVariable("postId")int id, Model model,
                                 @AuthenticationPrincipal UserDetails userDetails){
        Post post = postService.findPostById(id);
        model.addAttribute("post",post);
        if(userDetails != null) {
            model.addAttribute("currentUser", userDetails.getUsername());
        }
        return "post";
    }

    @GetMapping("/update/{postId}")
    public String update(@PathVariable("postId")int id, Model model){
        Post post = postService.findPostById(id);
        List<Tag> tags = post.getTags();
        List<User> authors = userService.findAuthors();

        StringBuilder tagAsString = new StringBuilder();
        for(Tag tempTag : tags){
            tagAsString.append(tempTag.getName()).append(",");
        }

        if (!tagAsString.isEmpty()) {
            tagAsString.deleteCharAt(tagAsString.length() - 1);
        }

        model.addAttribute("tagsAsString",tagAsString);
        model.addAttribute("post",post);
        model.addAttribute("presentPostId",post.getId());
        model.addAttribute("tags", tags);
        model.addAttribute("authors", authors);

        return "publish-form";
    }

    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId")int id){
        Post post = postService.findPostById(id);
        postService.deletePost(post);
        return "redirect:/";
    }

    @GetMapping("/filter/{pageNo}")
    public String applyFilter(@RequestParam(required = false) Set<String> tagNames,
                              @RequestParam(required = false) Set<String> authorsName, Model model,
                              @RequestParam(name="startDate", required = false) String startDate,
                              @RequestParam(name="endDate", required = false) String endDate,
                              @RequestParam(value = "field",required = false) String field,
                              @RequestParam(value = "sortDir",required = false) String sortDir,
                              @PathVariable("pageNo") int pageNo){
        Page<Post> page = postService.getPostsBySearchWithFilter(field, authorsName, tagNames, startDate, endDate,pageNo, sortDir);
        List<Post> posts = page.getContent();
        Set<Tag> tagsSet = new HashSet<>();
        Set<User> usersSet = new HashSet<>();

        for (Post post : posts) {
            tagsSet.addAll(post.getTags());
            usersSet.add(post.getAuthor());
        }

        List<User> users = new ArrayList<>(usersSet);
        List<Tag> tags = new ArrayList<>(tagsSet);

        model.addAttribute("posts", posts);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("field", field);
        model.addAttribute("tags", tags);
        model.addAttribute("posts", posts);
        model.addAttribute("authors", users);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("tagNames", tagNames);
        model.addAttribute("authorsName", authorsName);

        return "show-posts";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo, Model model){
        int pageSize = 10;
        Page<Post> page = postService.getPaginatedPosts(pageNo, pageSize);
        List<Post> posts = page.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNo);

        return "show-posts";
    }
}
