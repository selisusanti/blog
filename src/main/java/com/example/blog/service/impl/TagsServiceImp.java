package com.example.blog.service.impl;

import java.util.Date;
import java.util.Optional;

import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.request.TagsDTO;
import com.example.blog.dto.response.ResponseTagsDTO;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Tags;
import com.example.blog.repository.TagsRepository;
import com.example.blog.service.TagsService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagsServiceImp implements TagsService {
    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private TagsService tagsService;

    private static final String RESOURCE = "Tags";
    private static final String FIELD = "id";

    private ResponseTagsDTO fromEntity(Tags tags) {
        ResponseTagsDTO response = new ResponseTagsDTO();
        BeanUtils.copyProperties(tags, response);
        return response;
    }

    @Override
    public ResponseTagsDTO save(TagsDTO request) {
        try { 
            Optional<Tags> detailList = tagsRepository.findByName(request.getName());
            if (detailList.isPresent()) {
                Tags tags = tagsRepository.findByName(request.getName()).orElseThrow(() -> new ResourceNotFoundException(request.getName().toString(), FIELD, RESOURCE));
                return fromEntity(tags);
            }else{
                Tags tags = new Tags();
                tags.setName(request.getName().toLowerCase());
                tags.setCreated_at(new Date());
                tags.setUpdated_at(new Date());
                tagsRepository.save(tags);
                return fromEntity(tags);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseTagsDTO> findAll(Pageable pageable) {
        try {
            return tagsRepository.findAll(pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseTagsDTO> findByName(Pageable pageable, String param) {
        try {
            return tagsRepository.findByNameContaining(param, pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseTagsDTO findById(Long id) {
        try {
            Tags tags = tagsRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            return fromEntity(tags);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseTagsDTO deleteById(DeleteDTO request) {
        try {
            Tags tags = tagsRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(request.getId().toString(), FIELD, RESOURCE));
            tagsRepository.deleteById(request.getId());
            return fromEntity(tags);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseTagsDTO update(Long id, TagsDTO request) {
        try {
            Tags tags = tagsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));               
            tags.setName(request.getName());
            return fromEntity(tags);
        }catch(ResourceNotFoundException e){
            log.error(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


}