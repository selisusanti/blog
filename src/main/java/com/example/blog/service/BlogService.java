package com.example.blog.service;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Blog;
import com.example.blog.model.Tags;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BlogService{
    private final BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository){
        this.blogRepository = blogRepository;
    }

    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    public Page<Blog> findAll(Pageable pageable){
        return blogRepository.findAll(pageable);
    }

    public Optional<Blog> findById(Long id){
        return blogRepository.findById(id);
    }

    public Blog save(Blog blog){
        return blogRepository.save(blog);
    }

    public Blog update(Long id, Blog blog) {
        blog.setId(id);
        return blogRepository.save(blog);
    }
}