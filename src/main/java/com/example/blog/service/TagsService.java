package com.example.blog.service;

import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.request.TagsDTO;
import com.example.blog.dto.response.ResponseTagsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagsService{
    ResponseTagsDTO save(TagsDTO request);
    Page<ResponseTagsDTO> findAll(Pageable pageable);
    Page<ResponseTagsDTO> findByName(Pageable pageable, String param);
    ResponseTagsDTO findById(Long id);
    ResponseTagsDTO deleteById(DeleteDTO request);
    ResponseTagsDTO update(Long id, TagsDTO request);
}