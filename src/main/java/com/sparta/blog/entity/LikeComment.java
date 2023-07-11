package com.sparta.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "like_comments")
@IdClass(LikeCommentId.class)
public class LikeComment {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column
    private boolean isLike;

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }
}
