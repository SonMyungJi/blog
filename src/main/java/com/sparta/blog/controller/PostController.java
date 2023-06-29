package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    // 작성자(User) 정보를 JWT에서 받기 때문에 @AuthenticationPrincipal 추가
        return postService.createPost(requestDto, userDetails.getUser());
    }

    // 게시글 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    // 게시글 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(userDetails.getUser(), id, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public PostResponseDto deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(userDetails.getUser(), id);
        return new PostResponseDto(true);
    }

    // 댓글 작성
    @PostMapping("/posts/{id}/comments")
    public void addComment(@PathVariable Long id,
                            @RequestBody CommentRequestDto requestDto,
                            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.addComment(id, requestDto, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/posts/{id}/comments/{commentid}")
    public void updateComment(@PathVariable Long id, Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updateComment(id, commentId, requestDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/posts/{id}/comments/{commentid}")
    public CommentResponseDto deleteComment(@PathVariable Long id, Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deleteComment(id, commentId, userDetails.getUser());
        return new CommentResponseDto(true);
    }
}
