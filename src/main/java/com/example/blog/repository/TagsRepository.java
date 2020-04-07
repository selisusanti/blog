package com.example.blog.repository;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Tags;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
	Page<Tags> findAll(Pageable pageable);
	Page<Tags> findByNameContaining(String name, Pageable pageable);
}