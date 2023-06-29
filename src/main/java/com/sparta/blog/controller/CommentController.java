package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/comments")
    public CommentResponseDto createComment(@RequestParam Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.createComment(postId, requestDto, userDetails.getUser());
        return new CommentResponseDto(new Comment());
    }

    // 댓글 수정
    @PutMapping("/comments/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public CommentResponseDto deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(id, userDetails.getUser());
        return new CommentResponseDto(true);
    }
}