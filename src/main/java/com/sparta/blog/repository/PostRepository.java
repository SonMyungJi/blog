package com.sparta.blog.repository;

import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    Post findByUserAndId(User user, Long id);
}