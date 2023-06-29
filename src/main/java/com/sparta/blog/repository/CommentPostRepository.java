package com.sparta.blog.repository;

import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPostRepository extends JpaRepository<CommentPost, Long> {
    void deleteByComment(Comment comment);
}
