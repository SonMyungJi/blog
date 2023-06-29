package com.sparta.blog.repository;

import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByUserAndId(User user, Long id);
}
