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

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return blogService.createPost(requestDto);
    }

    // 게시물 목록
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return blogService.getPosts();
    }

    // 게시글 조회
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return blogService.getPost(id);
    }

    @PutMapping("/post/{id}/form/param")
    public Long updateMemo(@PathVariable Long id, @RequestParam int password, @RequestBody PostRequestDto requestDto) {
        return blogService.updatePost(id, password, requestDto);
    }

    @DeleteMapping("/post/{id}/form/param")
    public Long deletePost(@PathVariable Long id, @RequestParam int password) {
        return blogService.deletePost(id, password);
    }
}
