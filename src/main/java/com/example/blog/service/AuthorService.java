package com.example.blog.service;

import com.example.blog.dto.request.AuthorDTO;
import com.example.blog.dto.request.AuthorPasswordDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseAuthorDTO;
import com.example.blog.model.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService{

    ResponseAuthorDTO save(Author request);
    Page<ResponseAuthorDTO> findAll(Pageable pageable);
    Page<ResponseAuthorDTO> findByName(Pageable pageable, String param);
    ResponseAuthorDTO findById(Long id);
    ResponseAuthorDTO deleteById(DeleteDTO request);
    ResponseAuthorDTO update(Long id, AuthorDTO request);
    ResponseAuthorDTO updatePassword(Long id, AuthorPasswordDTO request);
    
}