package com.sparta.blog.dto;

import com.sparta.blog.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String author;
    private String contents;
    private int password;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.contents = post.getContents();
        this.password = post.getPassword();
    }

    public PostResponseDto() {
        this.id = id;
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.password = 000000;
    }
}
