package com.example.blog.service;

import java.util.HashMap;

import com.example.blog.dto.request.AuthorDTO;
import com.example.blog.dto.request.AuthorPasswordDTO;
import com.example.blog.dto.request.AuthorRequest;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseAuthorDTO;
import com.example.blog.dto.response.ResponseBaseDTO;
import com.example.blog.model.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface AuthorService{

    ResponseAuthorDTO save(AuthorRequest request);
    Page<ResponseAuthorDTO> findAll(Pageable pageable);
    Page<ResponseAuthorDTO> findByName(Pageable pageable, String param);
    ResponseAuthorDTO findById(Long id);
    ResponseAuthorDTO deleteById(DeleteDTO request);
    ResponseAuthorDTO update(Long id, AuthorDTO request);
    ResponseAuthorDTO updatePassword(Long id, AuthorPasswordDTO request);
    public Author findByUsername(String username);
}