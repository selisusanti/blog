package com.example.blog.service;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Blog;
import com.example.blog.model.Comment;
import com.example.blog.model.Tags;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService{
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public Page<Comment> findAll(Pageable pageable){
        return commentRepository.findAll(pageable);
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }

    public Comment update(Long id, Comment comment) {
        comment.setId(id);
        return commentRepository.save(comment);
    }
    
}