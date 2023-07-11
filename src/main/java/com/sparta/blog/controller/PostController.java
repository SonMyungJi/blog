package com.sparta.blog.controller;

import com.sparta.blog.dto.ApiResponseDto;
import com.sparta.blog.dto.PostListResponseDto;
import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(result);
    }

    // 게시글 목록 조회
    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getPosts() {
        PostListResponseDto result = postService.getPosts();
        return ResponseEntity.ok().body(result);
    }

    // 게시글 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto result = postService.getPost(id);
        return ResponseEntity.ok().body(result);
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            PostResponseDto result = postService.updatePost(userDetails.getUser(), id, requestDto);
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(userDetails.getUser(), id);
            return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/posts/{id}/like")
    public ResponseEntity<ApiResponseDto> togglePostLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            boolean isLiked = postService.toggleLike(id, userDetails.getUser());
            if (isLiked == false) {
                return ResponseEntity.ok().body(new ApiResponseDto("좋아요 취소", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.ok().body(new ApiResponseDto("좋아요", HttpStatus.OK.value()));
            }
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("권한이 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
