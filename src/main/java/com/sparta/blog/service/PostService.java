package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.*;
import com.sparta.blog.repository.CommentPostRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentPostRepository commentPostRepository;
    public PostService(PostRepository postRepository, CommentRepository commentRepository, CommentPostRepository commentPostRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.commentPostRepository = commentPostRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user)); // 게시글 저장
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream() // DB에서 조회한 List를 Stream으로 변환
                .map(PostResponseDto::new) // Stream 처리를 통해 Post를 PostResponseDto로 변환
                .toList(); // Stream을 List로 다시 변환
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(User user, Long id, PostRequestDto requestDto) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new NullPointerException("해당 글은 존재하지 않습니다.");
            }
        } else {
            post = findPost(id);
        }
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public void deletePost(User user, Long id) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new NullPointerException("해당 글은 존재하지 않습니다.");
            }
        } else {
            post = findPost(id);
        }
        postRepository.delete(post);
    }

    public void addComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = findPost(postId);
        Comment comment = commentRepository.save(new Comment(requestDto, user));
        commentPostRepository.save(new CommentPost(comment, post));
    }

    @Transactional
    public void updateComment(Long id, Long commentId, CommentRequestDto requestDto, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;
        Comment comment;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
            }
            comment = commentRepository.findByUserAndId(user, commentId);
            if (comment == null || !comment.getPost().getId().equals(id)) {
                throw new IllegalArgumentException("해당 댓글은 존재하지 않거나 해당 글에 속하지 않습니다.");
            }
        } else {
            post = findPost(id);
            comment = findComment(commentId);
        }

        comment.update(requestDto);
    }


    public void deleteComment(Long id, Long commentId, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Post post;
        Comment comment;

        if (userRoleEnum == UserRoleEnum.USER) {
            post = postRepository.findByUserAndId(user, id);
            if (post == null) {
                throw new IllegalArgumentException("해당 글은 존재하지 않습니다.");
            }
            comment = commentRepository.findByUserAndId(user, commentId);
            if (comment == null || !comment.getPost().getId().equals(id)) {
                throw new IllegalArgumentException("해당 댓글은 존재하지 않거나 해당 글에 속하지 않습니다.");
            }
        } else {
            post = findPost(id);
            comment = findComment(commentId);
        }

        commentPostRepository.deleteByComment(comment); // comment와 관련된 관계 삭제
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