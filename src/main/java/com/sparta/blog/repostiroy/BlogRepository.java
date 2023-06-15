package com.sparta.blog.service;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@RequestMapping("/api")
public class BlogRepository {

    private final JdbcTemplate jdbcTemplate;

    public BlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Post save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO post (title, author, contents, password) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, post.getTitle());
                    preparedStatement.setString(2, post.getAuthor());
                    preparedStatement.setString(3, post.getContents());
                    preparedStatement.setInt(4, post.getPassword());
                    return preparedStatement;
                },
                keyHolder);

        Long id = keyHolder.getKey().longValue();
        post.setId(id);

        return post;
    }

    public List<PostResponseDto> findAll() {
        String sql = "SELECT * FROM post";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String contents = rs.getString("contents");
            int password = rs.getInt("password");
            return new PostResponseDto(id, title, author, contents, password);
        });
    }

    public PostResponseDto find(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            String title = rs.getString("title");
            String author = rs.getString("author");
            String contents = rs.getString("contents");
            int password = rs.getInt("password");
            return new PostResponseDto(id, title, author, contents, password);
        }, id);
    }

    public void update(Long id, PostRequestDto requestDto) {
        String sql = "UPDATE post SET title = ?, author = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getAuthor(), requestDto.getContents(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Post findByIdAndPassword(Long id, int password) {
        String sql = "SELECT * FROM post WHERE id = ? AND password = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Post post = new Post();
                post.setTitle(resultSet.getString("title"));
                post.setAuthor(resultSet.getString("author"));
                post.setContents(resultSet.getString("contents"));
                post.setPassword(resultSet.getInt("password"));
                return post;
            } else {
                return null;
            }
        }, id, password);
    }
}