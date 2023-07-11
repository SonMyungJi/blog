package com.sparta.blog.repository;

import com.sparta.blog.entity.LikePost;
import com.sparta.blog.entity.LikePostId;
import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, LikePostId> {
    LikePost findByPostIdAndUser(Long PostId, User user);
}
