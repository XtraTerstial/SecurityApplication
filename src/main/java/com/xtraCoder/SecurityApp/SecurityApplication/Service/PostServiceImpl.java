package com.xtraCoder.SecurityApp.SecurityApplication.Service;

import com.xtraCoder.SecurityApp.SecurityApplication.dto.PostDTO;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.PostEntity;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import com.xtraCoder.SecurityApp.SecurityApplication.exception.ResourseNotFoundException;
import com.xtraCoder.SecurityApp.SecurityApplication.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    //This method fetches all posts from the database, maps them to PostDTO objects, and returns them as a list.
    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postEntity -> modelMapper.map(postEntity, PostDTO.class))
                .toList();
    }

    //This method takes a PostDTO object as input, maps it to a PostEntity,
    // saves it to the database, and returns the saved entity mapped back to a PostDTO.
    @Override
    public PostDTO createNewPost(PostDTO inputPost) {
        PostEntity postEntity = modelMapper.map(inputPost, PostEntity.class);
        return modelMapper.map(postRepository.save(postEntity), PostDTO.class);
    }

    @Override
    public PostDTO getPostById(Long postId) {
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(()-> new ResourseNotFoundException("Post not found with id: " + postId));
        return modelMapper.map(postEntity, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO inputPost, Long postId) {
        PostEntity olderPost = postRepository.findById(postId)
                .orElseThrow(()-> new ResourseNotFoundException("Post not found with id: " + postId));
        inputPost.setPostId(postId);
        //modelMapper.map(source, destination); -> copies properties from source to destination
        modelMapper.map(inputPost, olderPost);
        PostEntity savedPostEntity = postRepository.save(olderPost);
        return modelMapper.map(savedPostEntity, PostDTO.class);
    }
}
