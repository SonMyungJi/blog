package com.sparta.blog.controller;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {

    private final BlogService blogService;
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // 게시글 작성
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return blogService.createPost(requestDto);
    }

    // 게시글 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return blogService.getPosts();
    }

    // 게시글 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return blogService.getPost(id);
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return blogService.updatePost(id, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public PostResponseDto deletePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        blogService.deletePost(id, requestDto.getPassword());
        return new PostResponseDto(true);
    }
}
