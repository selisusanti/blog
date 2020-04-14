package com.example.blog.repository;

import org.springframework.transaction.annotation.Transactional;

import com.example.blog.model.Blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("mysql")
public interface BlogRepository extends JpaRepository<Blog, Long> {
	Page<Blog> findAll(Pageable pageable);

    @Query("select e from #{#entityName} e where e.title like %:title% ")
    Page<Blog> findByTitle(Pageable pageable, String title);

    Page<Blog> findByCategoriesId(Pageable pageable, Long id);
    Page<Blog> findByAuthorId(Pageable pageable, Long author_id);

    @Transactional(readOnly = true)
    @Query(value = "select blog.* from blog join blog_tags on blog.id = blog_tags.blog_id "
    +"join tags on tags.id = blog_tags.tags_id "
    +"where tags.name = :tags_name ", nativeQuery = true)
    Page<Blog> findByTagsName(Pageable pageable, String tags_name);

}