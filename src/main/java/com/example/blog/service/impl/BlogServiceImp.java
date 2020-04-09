package com.example.blog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.BlogDTO;
import com.example.blog.common.dto.exception.ResourceNotFoundException;
import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.ResponseBlogDTO;
import com.example.blog.model.Author;
import com.example.blog.model.Blog;
import com.example.blog.model.Categories;
import com.example.blog.model.Tags;
import com.example.blog.repository.AuthorRepository;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.repository.TagsRepository;
import com.example.blog.service.BlogService;
import com.example.blog.service.TagsService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BlogServiceImp implements BlogService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private BlogRepository blogRepository;

    private static final String RESOURCE = "Blog";
    private static final String FIELD = "id";

    private ResponseBlogDTO fromEntity(Blog blog) {
        ResponseBlogDTO response = new ResponseBlogDTO();
        BeanUtils.copyProperties(blog, response);
        return response;
    }


    @Override
    public ResponseBlogDTO save(BlogDTO request) {
        try {
            Author author = authorRepository.findById(request.getAuthor_id()).orElseThrow(() -> new ResourceNotFoundException(request.getAuthor_id().toString(), FIELD, RESOURCE));
            Categories categories = categoriesRepository.findById(request.getCategories_id()).orElseThrow(() -> new ResourceNotFoundException(request.getCategories_id().toString(), FIELD, RESOURCE)); 

            List<String> tagname = request.getTags_name();
            ArrayList<Tags> tags = new ArrayList<Tags>();

            for (String tag : tagname) {
                Optional<Tags> detailList = tagsRepository.findByName(tag);
                if (detailList.isPresent()) {
                    Tags val = tagsRepository.findByName(tag).orElseThrow(() -> new ResourceNotFoundException(tag.toString(), FIELD, RESOURCE)); 
                    tags.add(val);
                }else{
                    Tags newtags = new Tags();
                    newtags.setName(tag);
                    Tags tagssave = tagsRepository.save(newtags);
                    tags.add(tagssave);
                }
            }

            Blog blog = new Blog();
            blog.setAuthor(author);
            blog.setCategories(categories);
            blog.setTag(tags);
            blog.setTitle(request.getTitle());
            blog.setContent(request.getContent());
            blog.setCreated_at(new Date());
            blog.setUpdated_at(new Date());
            blogRepository.save(blog);
            return fromEntity(blog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }

    }

}