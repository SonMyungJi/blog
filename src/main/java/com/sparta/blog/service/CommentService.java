package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.LikeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final LikeCommentRepository likeCommentRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Post post = postService.findPost(requestDto.getPostId());
        Comment comment = new Comment(requestDto.getBody(), user);

        comment.setPost(post);

        var savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(id);

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }
        comment.setBody(requestDto.getBody());

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }
        commentRepository.delete(comment);
    }

    public boolean toggleLike(Long id, User user) {
        Comment comment = findComment(id);
        LikeComment like = likeCommentRepository.findByCommentIdAndUser(id, user);

        if(like == null) {
            like = new LikeComment();
            like.setUser(user);
            like.setComment(comment);
            like.setLike(true);
            likeCommentRepository.save(like);

            comment.addLike(like);
            commentRepository.save(comment);

            return true;
        } else {
            likeCommentRepository.delete(like);

            comment.removeLike(like);
            commentRepository.save(comment);

            return false;
        }
    }

    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));
    }
}