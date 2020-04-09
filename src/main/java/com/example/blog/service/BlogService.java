package com.example.blog.service;

import com.example.blog.common.dto.BlogDTO;
import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.ResponseBlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService{
    ResponseBlogDTO save(BlogDTO request);
    Page<ResponseBlogDTO> findAll(Pageable pageable);
    // Page<ResponseBlogDTO> findByName(Pageable pageable, String param);
    ResponseBlogDTO findById(Long id);
    // ResponseBlogDTO deleteById(DeleteDTO request);
    ResponseBlogDTO update(Long id, BlogDTO request);
    // Page<ResponseBlogDTO> findByCategoryId(Pageable pageable, Long id);

}
