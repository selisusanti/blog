package com.example.blog.repository;

import java.util.List;

import com.example.blog.model.Categories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

	Page<Categories> findByNameContaining(String name,Pageable pageable);
    // Optional<Categories> findById(Long id);
}