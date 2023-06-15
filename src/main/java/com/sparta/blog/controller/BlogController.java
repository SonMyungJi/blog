package com.sparta.blog.controller;

import com.sparta.blog.dto.PostRequestDto;
import com.sparta.blog.dto.PostResponseDto;
import com.sparta.blog.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {

    private final JdbcTemplate jdbcTemplate;
    public BlogController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        Post post = new Post(requestDto);
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

        PostResponseDto postResponseDto = new PostResponseDto(post);

        return postResponseDto;
    }

    // 게시물 목록
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
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

    // 게시글 조회
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            String title = rs.getString("title");
            String author = rs.getString("author");
            String contents = rs.getString("contents");
            int password = rs.getInt("password");
            return new PostResponseDto(id, title, author, contents, password);
        }, id);
    }



    @PutMapping("/post/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        Post post = findById(id);
        if(post != null) {

            String sql = "UPDATE post SET title = ?, author = ?, contents = ? password = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getAuthor(), requestDto.getContents(), requestDto.getPassword(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/post/{id}")
    public Long deletePost(@PathVariable Long id) {
        Post post = findById(id);
        if (post != null ) {
            String sql = "DELETE FROM post WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 글은 존재하지 않습니다.");
        }
    }

    private Post findById(Long id) {
        String sql = "SELECT * FROM post WHERE id = ?";

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
        }, id);
    }

//    private void matchesPassword(String password, Post post) {
//        PostResponseDto postResponseDto = new PostResponseDto(post);
//        String storedPassword = postResponseDto.getPassword();
//
//        if (password.equals(storedPassword)) {
//            System.out.println("비밀번호가 일치합니다.");
//        } else {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//    }


}
