package com.example.blog.service;
import java.util.List;
import java.util.Optional;

import com.example.blog.model.Tags;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TagsService{
    private final TagsRepository tagsRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository){
        this.tagsRepository = tagsRepository;
    }

    public List<Tags> findAll(){
        return tagsRepository.findAll();
    }

    public List<Tags> findByNameContaining(String name, Pageable pageable){
        return tagsRepository.findByNameContaining(name, pageable);
    }

    public Optional<Tags> findById(Long id){
        return tagsRepository.findById(id);
    }

    public Tags save(Tags tags){
        return tagsRepository.save(tags);
    }

    public void deleteById(Long id) {
        tagsRepository.deleteById(id);
    }


}