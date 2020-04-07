package com.example.blog.service;

import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.response.ResponseTagsDTO;
import com.example.blog.model.Tags;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagsService{
    private final TagsRepository tagsRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository){
        this.tagsRepository = tagsRepository;
    }

    public Page<ResponseTagsDTO> findAll(Pageable pageable){
        try {
            return tagsRepository.findAll(pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public Page<ResponseTagsDTO> findByNameContaining(String name, Pageable pageable){
        return tagsRepository.findByNameContaining(name, pageable).map(this::fromEntity);
    }

    public Optional<Tags> findById(Long id){
        return tagsRepository.findById(id);
    } 
    
    public Optional<Tags> findByName(String name){
        return tagsRepository.findByName(name);
    }

    private ResponseTagsDTO fromEntity(Tags tags) {
        ResponseTagsDTO response = new ResponseTagsDTO();
        BeanUtils.copyProperties(tags, response);
        return response;
    }

    public Tags save(Tags tags){
        return tagsRepository.save(tags);
    }

    public void deleteById(Long id) {
        tagsRepository.deleteById(id);
    }


}