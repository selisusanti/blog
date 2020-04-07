package com.example.blog.repository;

import java.util.List;

import com.example.blog.model.Categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

	List<Categories> findByName(String name);
    // Optional<Categories> findById(Long id);
}