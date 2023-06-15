package com.sparta.blog.controller;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.service.BlogService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {

    private final JdbcTemplate jdbcTemplate;
    public BlogController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        BlogService blogService = new BlogService(jdbcTemplate);
        return blogService.createPost(requestDto);
    }

    // 게시물 목록
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        BlogService blogService = new BlogService(jdbcTemplate);
        return blogService.getPosts();
    }

    // 게시글 조회
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        BlogService blogService = new BlogService(jdbcTemplate);
        return blogService.getPost(id);
    }


    @PutMapping("/post/{id}/{password}")
    public Long updateMemo(@PathVariable Long id, @PathVariable int password, @RequestBody PostRequestDto requestDto) {
        BlogService blogService = new BlogService(jdbcTemplate);
        return blogService.updatePost(id, password, requestDto);
    }

    @DeleteMapping("/post/{id}/{password}")
    public Long deletePost(@PathVariable Long id, @PathVariable int password) {
        BlogService blogService = new BlogService(jdbcTemplate);
        return blogService.deletePost(id, password);
    }
}
