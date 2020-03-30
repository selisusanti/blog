package com.example.blog.repository;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Tags;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository <Tags, Long> {

	List<Tags> findAll();
	
	List<Tags> findByNameContaining(String name, Pageable pageable);
    // Optional<Tags> findById(Long id);
}