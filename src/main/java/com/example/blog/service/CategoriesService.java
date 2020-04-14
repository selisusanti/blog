package com.example.blog.service;


import com.example.blog.common.dto.CategoriesDTO;
import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.ResponseCategoriesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriesService{
    ResponseCategoriesDTO save(CategoriesDTO request);
    Page<ResponseCategoriesDTO> findAll(Pageable pageable);
    Page<ResponseCategoriesDTO> findByName(Pageable pageable, String param);
    ResponseCategoriesDTO findById(Long id);
    ResponseCategoriesDTO deleteById(DeleteDTO request);
    ResponseCategoriesDTO update(Long id, CategoriesDTO request);
}