package com.example.blog.service.impl;

import java.util.Date;

import com.example.blog.dto.request.CategoriesDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseCategoriesDTO;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Categories;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.service.CategoriesService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoriesServiceImp implements CategoriesService {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private CategoriesService categoriesService;

    private static final String RESOURCE = "Categories";
    private static final String FIELD = "id";

    private ResponseCategoriesDTO fromEntity(Categories categories) {
        ResponseCategoriesDTO response = new ResponseCategoriesDTO();
        BeanUtils.copyProperties(categories, response);
        return response;
    }

    @Override
    public ResponseCategoriesDTO save(CategoriesDTO request) {
        try {
            Categories categories = new Categories();
            categories.setName(request.getName().toLowerCase());
            categories.setCreated_at(new Date());
            categories.setUpdated_at(new Date());
            categoriesRepository.save(categories);
            return fromEntity(categories);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseCategoriesDTO> findAll(Pageable pageable) {
        try {
            return categoriesRepository.findAll(pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseCategoriesDTO> findByName(Pageable pageable, String param) {
        try {
            return categoriesRepository.findByNameContaining(param, pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseCategoriesDTO findById(Long id) {
        try {
            Categories categories = categoriesRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            return fromEntity(categories);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseCategoriesDTO deleteById(DeleteDTO request) {
        try {
            Categories categories = categoriesRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(request.getId().toString(), FIELD, RESOURCE));
            categoriesRepository.deleteById(request.getId());
            return fromEntity(categories);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseCategoriesDTO update(Long id, CategoriesDTO request) {
        try {
            Categories categories = categoriesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));               
            categories.setName(request.getName());
            return fromEntity(categories);
        }catch(ResourceNotFoundException e){
            log.error(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

}