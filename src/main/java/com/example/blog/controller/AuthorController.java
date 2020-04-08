package com.example.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.blog.common.dto.AuthorDTO;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.service.AuthorService;

@RestController
public class AuthorController {
    
    @Autowired
    AuthorService AuthorService;

    @RequestMapping(value = "/authors", method = RequestMethod.POST)
    public ResponseBaseDTO createAuthors(@Valid @RequestBody AuthorDTO request) {
        return ResponseBaseDTO.ok(AuthorService.save(request));
    }

}