package com.example.blog.controller;

import com.example.blog.repository.AuthorRepository;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.service.AuthorService;
import com.example.blog.service.BlogService;
import com.example.blog.service.CategoriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Author;
import com.example.blog.model.Blog;
import com.example.blog.model.Categories;
import com.example.blog.model.ResponseBaseDTO;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listTags(){
        ResponseBaseDTO response = new ResponseBaseDTO();         
        
        try
        {         
            List<Blog> blogs = blogService.findAll();
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(blogs);         
            
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        catch(Exception e)
        {
         // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }        
    }


    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> getTagsById(@PathVariable("id") long id) {

        ResponseBaseDTO response = new ResponseBaseDTO(); 
        try
        {             
            Optional<Blog> blog = blogService.findById(id); 
            if (blog.isPresent()) {           
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(blog);     
                
            }
            return new ResponseEntity<>( response, HttpStatus.OK);
        }
        catch(Exception e)
        {
            // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDTO> create(@RequestBody Blog blog) throws NotFoundException{
        
        // Blog result = new Blog();
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        Author author = authorRepository.findById(blog.getAuthor_id()).orElseThrow(() -> new NotFoundException("Author id " + blog.getAuthor_id() + " NotFound"));
        Categories categories = categoriesRepository.findById(blog.getCategories_id()).orElseThrow(() -> new NotFoundException("Categories id " + blog.getCategories_id() + " NotFound"));

        // Optional<Categories> categories = categoriesService.findById(blog.getCategories_id());
        
        // Categories categories = categoriesService.findById(blog.getCategories_id());        

        blog.setAuthor(author);
        blog.setCategories(categories);
        
        try{
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(blogService.save(blog));           
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }catch(Exception e){
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }
}