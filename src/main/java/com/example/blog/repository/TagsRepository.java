package com.example.blog.repository;

import com.example.blog.model.Tags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {

}