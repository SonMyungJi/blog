package com.sparta.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "like_posts")
@IdClass(LikePostId.class)
public class LikePost {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private boolean isLike;

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }

}
