package com.sparta.blog.repository;

import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    void deleteByComment(Comment comment);
}
