package com.example.blog.service;

import java.util.Optional;

import com.example.blog.common.dto.CommentDTO;
import com.example.blog.common.dto.response.ResponseCommentDTO;
import com.example.blog.model.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    // Optional<Comment> findByBlogId(Long id);
    // Comment save(Comment comment);
    ResponseCommentDTO save(CommentDTO request, Long id);
    Page<ResponseCommentDTO> findAllByBlogId(Pageable pageable, Long id);
    Page<ResponseCommentDTO> findByName(Pageable pageable, String name);
    ResponseCommentDTO findByBlogId(Long blog,Long id);
}