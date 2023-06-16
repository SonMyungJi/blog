package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        return blogRepository.findAll().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return covertToPostResponseDto(post);
    }

    private PostResponseDto covertToPostResponseDto(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setAuthor(post.getAuthor());
        postResponseDto.setContents(post.getContents());

        return postResponseDto;
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

    private Post findPost(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 글은 존재하지 않습니다."));
    }

    private Post findPostByIdAndPassword(Long id, int password) {
        return blogRepository.findByIdAndPassword(id, password);
    }
}