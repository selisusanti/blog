package com.example.blog.service;

import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.response.ResponseAuthorDTO;
import com.example.blog.model.Author;
import com.example.blog.repository.AuthorRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class AuthorService{
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    private ResponseAuthorDTO fromEntity(Author author) {
        ResponseAuthorDTO response = new ResponseAuthorDTO();
        BeanUtils.copyProperties(author, response);
        return response;
    }

    public Page<ResponseAuthorDTO> findAll(Pageable pageable){
        try {
            return authorRepository.findAll(pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
 
    public List<Author> findByName(String username){
        return authorRepository.findByUsername(username);
    }

    public Optional<Author> findById(Long id){
        return authorRepository.findById(id);
    }

    public Author save(Author author){
        return authorRepository.save(author);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

}