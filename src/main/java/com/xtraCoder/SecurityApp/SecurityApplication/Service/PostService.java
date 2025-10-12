package com.xtraCoder.SecurityApp.SecurityApplication.Service;

import com.xtraCoder.SecurityApp.SecurityApplication.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long postId);

    PostDTO updatePost(PostDTO inputPost, Long postId);
}

