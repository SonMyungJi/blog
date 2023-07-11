package com.sparta.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<LikeComment> likes;

    @Column
    private Long likeCount;

    public Comment(String body, User user) {
        this.body = body;
        this.user = user;
        this.likeCount = 0L;
    }

    public void setBody(String body) { this.body = body; }
    public void setPost(Post post) { this.post = post; }

    public void addLike(LikeComment like) {
        likes.add(like);
        likeCount++;
    }
    public void removeLike(LikeComment like) {
        likes.remove(like);
        likeCount--;
    }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
}