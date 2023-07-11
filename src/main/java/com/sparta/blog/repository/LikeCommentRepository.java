package com.sparta.blog.repository;

import com.sparta.blog.entity.LikeComment;
import com.sparta.blog.entity.LikeCommentId;
import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, LikeCommentId> {
    LikeComment findByCommentIdAndUser(Long CommentId, User user);
}
