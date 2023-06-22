package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequestMapping("/api")
public class BlogService {

    private final BlogRepository blogRepository;
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    } // 제어의 역전

    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto); // 게시글 생성
        Post savePost = blogRepository.save(post); // 게시글 저장

        return new PostResponseDto(savePost);
    }

    public List<PostResponseDto> getPosts() {
        return blogRepository.findAllByOrderByCreatedAtDesc().stream() // DB에서 조회한 List를 Stream으로 변환
                .map(PostResponseDto::new) // Stream 처리를 통해 Post를 PostResponseDto로 변환
                .toList(); // Stream을 List로 다시 변환
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = findPost(id);

        post.checkPassword(requestDto.getPassword());
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public void deletePost(Long id, String password) {
        Post post = findPost(id);
        post.checkPassword(password);
        blogRepository.delete(post);
        }

    private Post findPost(Long id) {
        return blogRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 글은 존재하지 않습니다."));
    }
}