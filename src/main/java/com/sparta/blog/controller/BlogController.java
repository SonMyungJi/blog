package com.sparta.blog.controller;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BlogController {

    private final Map<Long, Post> postList = new HashMap<>();

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        Long maxId = postList.size() > 0 ? Collections.max(postList.keySet()) + 1 : 1;
        post.setId(maxId);
        postList.put(post.getId(), post);
        PostResponseDto postResponseDto = new PostResponseDto(post);

        return postResponseDto;
    }

    // 게시물 목록
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        List<PostResponseDto> responseList = postList.values().stream().map(PostResponseDto::new).toList();

        return responseList;
    }

    // 게시글 조회
    @GetMapping("/post/{id}")
    public Long getPost(@PathVariable Long id) {
        if(postList.containsKey(id)) {
            postList.get(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    @PutMapping("/post/{id}")
    public Long updateMemo(@PathVariable Long id, String password, @RequestBody PostRequestDto requestDto) {
        if(postList.containsKey(id)) {
            Post post = postList.get(id);
            post.update(requestDto);
            return post.getId();
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/post/{id}")
    public Long deletePost(@PathVariable Long id, String password) {
        if(postList.containsKey(id)) {
            postList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

}
