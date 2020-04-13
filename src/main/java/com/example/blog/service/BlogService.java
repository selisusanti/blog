package com.example.blog.service;

import com.example.blog.common.dto.BlogDTO;
import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.BaseResponseDTO;
import com.example.blog.common.dto.response.BlogResponse;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.common.dto.response.ResponseBlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService{
    ResponseBlogDTO save(BlogDTO request);
    Page<ResponseBlogDTO> findAll(Pageable pageable);
    Page<ResponseBlogDTO> findByTitle(Pageable pageable, String title);
    Page<ResponseBlogDTO> findByCategoriesId(Pageable pageable, Long category_id);
    Page<ResponseBlogDTO> findByAuthorId(Pageable pageable, Long author_id);
    Page<ResponseBlogDTO> findByTagsName(Pageable pageable, String tags_name);
    ResponseBlogDTO findById(Long id);
    BaseResponseDTO<BlogResponse> delete(DeleteDTO request);
    ResponseBlogDTO update(Long id, BlogDTO request);
    // Page<ResponseBlogDTO> findByCategoryId(Pageable pageable, Long id);

}
