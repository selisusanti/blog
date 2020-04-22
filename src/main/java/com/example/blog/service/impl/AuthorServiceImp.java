package com.example.blog.service.impl;

import java.util.Date;

import com.example.blog.dto.request.AuthorDTO;
import com.example.blog.dto.request.AuthorPasswordDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseAuthorDTO;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Author;
import com.example.blog.repository.AuthorRepository;
import com.example.blog.service.AuthorService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorServiceImp implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    private static final String RESOURCE = "Author";
    private static final String FIELD = "id";

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
    public ResponseAuthorDTO save(Author request) {

        try {
            Author author = new Author();
            author.setUsername(request.getUsername().toLowerCase());
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

    @Override
    public Page<ResponseAuthorDTO> findAll(Pageable pageable) {
        try {
            return authorRepository.findAll(pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseAuthorDTO> findByName(Pageable pageable, String param) {
        try {
            param = param.toLowerCase();
            return authorRepository.findByName(pageable, param).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseAuthorDTO findById(Long id) {
        try {
            Author authors = authorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            return fromEntity(authors);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseAuthorDTO deleteById(DeleteDTO request) {
        try {
            Author authors = authorRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(request.getId().toString(), FIELD, RESOURCE));
            authorRepository.deleteById(request.getId());
            return fromEntity(authors);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ResponseAuthorDTO update(Long id, AuthorDTO request) {
        try{
            Author authors = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            authors.setFirst_name(request.getFirst_name());
            authors.setLast_name(request.getLast_name());
            authors.setUsername(request.getUsername());
            authorRepository.save(authors);
            return fromEntity(authors);
        }catch(ResourceNotFoundException e){
            log.error(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public ResponseAuthorDTO updatePassword(Long id, AuthorPasswordDTO request) {
        try{
            Author authors = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            authors.setFirst_name(request.getPassword());
            authorRepository.save(authors);
            return fromEntity(authors);
        }catch(ResourceNotFoundException e){
            log.error(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}