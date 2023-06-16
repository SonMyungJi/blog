package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequestMapping("/api")
public class BlogService {

    private final BlogRepository blogRepository;
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        Post savePost = blogRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(savePost);

        return postResponseDto;
    }

    public List<PostResponseDto> getPosts() {
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Optional<Post> optionalPost = blogRepository.findById(id);
        Post post = optionalPost.orElseThrow(() -> new IllegalArgumentException("선택한 글은 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    @Transactional
    public Long updatePost(Long id, int password, PostRequestDto requestDto) {
        Post post = findPostByIdAndPassword(id, password);
        post.update(requestDto);
        return id;
    }

    public Long deletePost(Long id, int password) {
        Post post = findPostByIdAndPassword(id, password);
        blogRepository.delete(post);
        return id;
        }

    private Post findPostByIdAndPassword(Long id, int password) {
        Post post = blogRepository.findByIdAndPassword(id, password);

        if (post == null) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return post;
    }
}