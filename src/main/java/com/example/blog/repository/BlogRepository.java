package com.example.blog.repository;

import com.example.blog.model.Blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
	Page<Blog> findAll(Pageable pageable);

    @Query("select e from #{#entityName} e where e.title like %:title% ")
    Page<Blog> findByTitle(Pageable pageable, String title);

    Page<Blog> findByCategoriesId(Pageable pageable, Long id);
    Page<Blog> findByAuthorId(Pageable pageable, Long author_id);
}