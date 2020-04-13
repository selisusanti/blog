package com.example.blog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.BlogDTO;
import com.example.blog.common.dto.exception.ResourceNotFoundException;
import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.BaseResponseDTO;
import com.example.blog.common.dto.response.BlogAuthorResponse;
import com.example.blog.common.dto.response.BlogCategoriesResponse;
import com.example.blog.common.dto.response.BlogResponse;
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


    @Override
    public Page<ResponseBlogDTO> findAll(Pageable pageable) {
        try {
            return blogRepository.findAll(pageable).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseBlogDTO> findByTitle(Pageable pageable, String title) {
        try {
            return blogRepository.findByTitle(pageable, title).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public ResponseBlogDTO update(Long id, BlogDTO request) {
        try{
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            blog.setTitle(request.getTitle());
            blog.setContent(request.getContent());
            blogRepository.save(blog);
            return fromEntity(blog);
        }catch(ResourceNotFoundException e){
            log.error(e.getMessage(), e);
            throw e;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public ResponseBlogDTO findById(Long id) {
        try {
            Blog blog = blogRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            return fromEntity(blog);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public BaseResponseDTO<BlogResponse> delete(DeleteDTO request) {
        try {
            Blog blog = blogRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(request.getId().toString(), FIELD, RESOURCE));
            blogRepository.deleteById(request.getId());

            BlogResponse blogResponse = new BlogResponse();
            blogResponse.setId(blog.getId());
            blogResponse.setTitle(blog.getTitle());
            blogResponse.setContent(blog.getContent());
            blogResponse.setCreated_at(blog.getCreated_at());
            blogResponse.setUpdated_at(blog.getUpdated_at());
            
            // Author
            BlogAuthorResponse authorResponse = new BlogAuthorResponse();
            authorResponse.setFirstname(blog.getAuthor().getFirst_name());
            authorResponse.setLastname(blog.getAuthor().getLast_name());
            blogResponse.setAuthor(authorResponse);

            // Category
            BlogCategoriesResponse categoriesResponse = new BlogCategoriesResponse();
            categoriesResponse.setName(blog.getCategories().getName());
            blogResponse.setCategories(categoriesResponse);
            
            return BaseResponseDTO.ok(blogResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseDTO.error("500", "Failed to delete post.");
        }
    }

    @Override
    public Page<ResponseBlogDTO> findByCategoriesId(Pageable pageable, Long id) {
        try {
            return blogRepository.findByCategoriesId(pageable, id).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseBlogDTO> findByAuthorId(Pageable pageable, Long author_id) {
        try {
            return blogRepository.findByAuthorId(pageable, author_id).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }



}