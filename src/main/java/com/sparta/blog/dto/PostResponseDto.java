package com.sparta.blog.dto;

import com.sparta.blog.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String author;
    private String contents;
    private String password;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.contents = post.getContents();
        this.password = post.getPassword();
    }
}
