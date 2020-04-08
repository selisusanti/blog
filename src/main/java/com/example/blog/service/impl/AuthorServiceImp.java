package com.example.blog.service.impl;

import java.util.Date;

import com.example.blog.common.dto.AuthorDTO;
import com.example.blog.common.dto.response.ResponseAuthorDTO;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.model.Author;
import com.example.blog.repository.AuthorRepository;
import com.example.blog.service.AuthorService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorServiceImp implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private ResponseAuthorDTO fromEntity(Author author) {
        ResponseAuthorDTO response = new ResponseAuthorDTO();
        BeanUtils.copyProperties(author, response);
        return response;
    }
    
    @Override
    public ResponseAuthorDTO save(AuthorDTO request) {

        try {
            Author author = new Author();

            author.setUsername(request.getUsername());
            author.setFirst_name(request.getFirst_name());
            author.setLast_name(request.getLast_name());
            author.setPassword(passwordEncoder().encode(request.getPassword()));
            author.setCreated_at(new Date());
            author.setUpdated_at(new Date());
            authorRepository.save(author);
            return fromEntity(author);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}