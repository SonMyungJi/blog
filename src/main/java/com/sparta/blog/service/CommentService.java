package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.PostCommentRepository;
import com.sparta.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, PostCommentRepository postCommentRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
    }

    public void createComment(Long id, CommentRequestDto requestDto, User user) {
        Post post = findPost(id);
        Comment comment = commentRepository.save(new Comment(requestDto, user));
        postCommentRepository.save(new PostComment(post, comment));
    }

    @Transactional
    public void updateComment(Long id, CommentRequestDto requestDto, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;
        Comment comment;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
            }
            comment = commentRepository.findByUserAndId(user, id);
            if (comment == null) {
                throw new IllegalArgumentException("해당 댓글은 존재하지 않습니다.");
            }
        } else {
            post = findPost(id);
            comment = findComment(id);
        }

        comment.update(requestDto);
    }


    public void deleteComment(Long id, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;
        Comment comment;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
            }
            comment = commentRepository.findByUserAndId(user, id);
            if (comment == null) {
                throw new IllegalArgumentException("해당 댓글은 존재하지 않습니다.");
            }
        } else {
            post = findPost(id);
            comment = findComment(id);
        }

        postCommentRepository.deleteByComment(comment); // comment와 관련된 관계 삭제
        commentRepository.delete(comment); // comment 삭제
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 글은 존재하지 않습니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));
    }
}