package com.xtraCoder.SecurityApp.SecurityApplication.controller;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.PostService;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postServices;

    @GetMapping
    public List<PostDTO> getAllPosts(){
        return postServices.getAllPosts();
    }

    @GetMapping("/{postId}")
    public PostDTO getPostBYId(@PathVariable Long postId){
        return postServices.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost){
        return postServices.createNewPost(inputPost);
    }

    @PutMapping("/{postId}")
    public PostDTO updatePost(@RequestBody PostDTO inputPost, @PathVariable Long postId){
        return postServices.updatePost(inputPost, postId);
    }
}
