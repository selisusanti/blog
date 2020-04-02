package com.example.blog.service;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Categories;
import com.example.blog.model.Tags;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService{
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }

    public Page<Categories> findAll(Pageable pageable){
        return categoriesRepository.findAll(pageable);
    }

    public Page<Categories> findByNameContaining(String name,Pageable pageable){
        return categoriesRepository.findByNameContaining(name,pageable);
    }

    public Optional<Categories> findById(Long id){
        return categoriesRepository.findById(id);
    }


    public Categories save(Categories categories){
        return categoriesRepository.save(categories);
    }

    public void deleteById(Long id) {
        categoriesRepository.deleteById(id);
    }

}