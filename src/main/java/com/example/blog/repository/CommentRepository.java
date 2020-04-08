package com.example.blog.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.example.blog.model.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByBlogId(Pageable pageable, Long id);
    
    @Query("select e from #{#entityName} e where e.content like %:param% ")
    Page<Comment> findByName(Pageable pageable, String param);


    @Query("select e from #{#entityName} e where blog.id =:blog AND id =:id ")
    Comment findByBlogId(Long blog, Long id);

    @Modifying
	@Transactional
	@Query("DELETE From Comment WHERE blog.id =:id")
    void deleteAllPostByID(Long id);
}