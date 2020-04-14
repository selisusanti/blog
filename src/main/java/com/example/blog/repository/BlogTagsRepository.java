package com.example.blog.repository;

import org.springframework.transaction.annotation.Transactional;

import com.example.blog.model.BlogTags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogTagsRepository extends JpaRepository<BlogTags, Long> {

    @Transactional
    @Modifying
    @Query("delete from #{#entityName} where blog_id = :id ")
    void deleteBlogTagsByBlogId(Long id);
    
}