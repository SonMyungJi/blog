package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequestMapping("/api")
public class BlogService {

    private final JdbcTemplate jdbcTemplate;

    public BlogService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);

        com.sparta.blog.service.BlogRepository blogRepository = new com.sparta.blog.service.BlogRepository(jdbcTemplate);
        Post savePost = blogRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(savePost);

        return postResponseDto;
    }

    public List<PostResponseDto> getPosts() {
        com.sparta.blog.service.BlogRepository blogRepository = new com.sparta.blog.service.BlogRepository(jdbcTemplate);
        return blogRepository.findAll();
    }

    public PostResponseDto getPost(Long id) {
        com.sparta.blog.service.BlogRepository blogRepository = new com.sparta.blog.service.BlogRepository(jdbcTemplate);
        return blogRepository.find(id);
    }

    public Long updatePost(Long id, int password, PostRequestDto requestDto) {
        com.sparta.blog.service.BlogRepository blogRepository = new com.sparta.blog.service.BlogRepository(jdbcTemplate);
        Post post = blogRepository.findByIdAndPassword(id, password);
        if (post != null) {
            blogRepository.update(id, requestDto);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    public Long deletePost(Long id, int password) {
        com.sparta.blog.service.BlogRepository blogRepository = new com.sparta.blog.service.BlogRepository(jdbcTemplate);
        Post post = blogRepository.findByIdAndPassword(id, password);
        if(post != null) {
            blogRepository.delete(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }
}