package com.sparta.blog.repository;

import com.sparta.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("/api")
public interface BlogRepository extends JpaRepository<Post, Long> {
    Post findByIdAndPassword(Long id, int password);
}