package com.example.blog.controller;

import com.example.blog.model.Tags;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagsController{

    @Autowired
    private TagsRepository tagsRepository; 

    @GetMapping("/tags")
    public Page<Tags> getAllPage(Pageable pageable){
        return tagsRepository.findAll(pageable);
    }


}