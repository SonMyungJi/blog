package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.PostCommentRepository;
import com.sparta.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    public PostService(PostRepository postRepository, PostCommentRepository postCommentRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user)); // 게시글 저장
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream() // DB에서 조회한 List를 Stream으로 변환
                .map(PostResponseDto::new) // Stream 처리를 통해 Post를 PostResponseDto로 변환
                .toList(); // Stream을 List로 다시 변환
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(User user, Long id, PostRequestDto requestDto) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
            }
        } else {
            post = findPost(id);
        }
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(User user, Long id) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
            }
        } else {
            post = findPost(id);
        }
        postCommentRepository.deleteByPost(post);
        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 글은 존재하지 않습니다."));
    }
}