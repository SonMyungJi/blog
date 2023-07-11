package com.sparta.blog.service;

import com.sparta.blog.dto.PostListResponseDto;
import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.LikePost;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.LikePostRepository;
import com.sparta.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user)); // 게시글 저장
        return new PostResponseDto(post);
    }

    public PostListResponseDto getPosts() {
        List<PostResponseDto> postList = postRepository.findAllByOrderByCreatedAtDesc().stream() // DB에서 조회한 List를 Stream으로 변환
                .map(PostResponseDto::new) // Stream 처리를 통해 Post를 PostResponseDto로 변환
                .toList(); // Stream을 List로 다시 변환

        return new PostListResponseDto(postList);
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(User user, Long id, PostRequestDto requestDto) {
        Post post = findPost(id);

        // 게시글 작성자와 요청자가 같은지 혹은 admin인지 확인
        if (!(post.getUser().equals(user) || (user.getRole().equals(UserRoleEnum.ADMIN)))) {
            throw new RejectedExecutionException();
        }

        post.setTitle(requestDto.getTitle());
        post.setContents(requestDto.getContents());

        return new PostResponseDto(post);
    }

    public void deletePost(User user, Long id) {
        Post post = findPost(id);

        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !post.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }
        postRepository.delete(post);
    }

    public boolean toggleLike(Long id, User user) {
        Post post = findPost(id);
        LikePost like = likePostRepository.findByPostIdAndUser(id, user);

        if(like == null) {
            like = new LikePost();
            like.setUser(user);
            like.setPost(post);
            like.setLike(true);

            likePostRepository.save(like);

            post.addLike(like);
            postRepository.save(post);

            return true;
        } else {
            likePostRepository.delete(like);

            post.removeLike(like);
            postRepository.save(post);

            return false;
        }
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 글은 존재하지 않습니다."));
    }
}