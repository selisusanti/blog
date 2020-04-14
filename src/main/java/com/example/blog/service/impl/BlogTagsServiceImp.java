package com.example.blog.service.impl;

import com.example.blog.repository.BlogTagsRepository;
import com.example.blog.service.BlogTagsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlogTagsServiceImp implements BlogTagsService {

    @Autowired
    private BlogTagsRepository blogTagsRepository;
    
    @Override
    public void deleteBlogTagsByBlogId(Long id) {
        try{
            blogTagsRepository.deleteBlogTagsByBlogId(id);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}