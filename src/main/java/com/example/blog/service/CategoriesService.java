package com.example.blog.service;
import java.util.List;
import java.util.Optional;

import com.example.blog.model.Categories;
import com.example.blog.model.Tags;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService{
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }

    public List<Categories> findAll(){
        return categoriesRepository.findAll();
    }

    public List<Categories> findByName(String name){
        return categoriesRepository.findByName(name);
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