package com.example.blog.repository;

import com.example.blog.model.Categories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    @Query("select e from #{#entityName} e where e.name like %:param% ")
    Page<Categories> findByName(Pageable pageable, String param);

	Page<Categories> findByNameContaining(String param,Pageable pageable);
}