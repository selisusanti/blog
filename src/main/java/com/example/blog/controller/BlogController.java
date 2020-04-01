package com.example.blog.controller;

import com.example.blog.repository.AuthorRepository;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.repository.TagsRepository;
import com.example.blog.service.AuthorService;
import com.example.blog.service.BlogService;
import com.example.blog.service.CategoriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.blog.model.Author;
import com.example.blog.model.Blog;
import com.example.blog.model.Categories;
import com.example.blog.model.ResponseBaseDTO;
import com.example.blog.model.Tags;

@RestController
@RequestMapping("/blogs")
public class BlogController {


    @Autowired
    BlogRepository blogRepository;

    @Autowired
    BlogService blogService;

    @Autowired
    TagsRepository tagsRepository; 

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> ListBlog(Pageable pageable){
        ResponseBaseDTO response = new ResponseBaseDTO();         
        
        try
        {         
            Page<Blog> blogs = blogService.findAll(pageable);
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


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> getBlogById(@PathVariable("id") long id) {

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
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseBaseDTO> updateBlog(@PathVariable("id") long id, @RequestBody Blog blog) throws NotFoundException{
       
        ResponseBaseDTO response = new ResponseBaseDTO();
        Blog _blog       = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog id " + id + " NotFound"));
          
        try {
            
                _blog.setTitle(blog.getTitle());
                _blog.setContent(blog.getContent());

                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");  
                response.setData(blogService.update(id, _blog));     

                return new ResponseEntity<>( response, HttpStatus.OK);
          
        } catch (Exception e) {
            // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage( "id " + id + " not exists! " );
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDTO> createBlog(@RequestBody Blog blog) throws NotFoundException{
        
        // Blog result = new Blog();
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        Author author = authorRepository.findById(blog.getAuthor_id()).orElseThrow(() -> new NotFoundException("Author id " + blog.getAuthor_id() + " NotFound"));
        Categories categories = categoriesRepository.findById(blog.getCategories_id()).orElseThrow(() -> new NotFoundException("Categories id " + blog.getCategories_id() + " NotFound"));  

        List<Long> tagtag = blog.getTags_id();
        ArrayList<Tags> tags = new ArrayList<Tags>();

        for (Long tag : tagtag) {
            Tags val = tagsRepository.findById(tag).orElseThrow(() -> new NotFoundException("Tags id " + tag + " NotFound"));
            tags.add(val);
        }

        blog.setAuthor(author);
        blog.setCategories(categories);
        blog.setTag(tags);
        
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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public  ResponseEntity<ResponseBaseDTO> delete(@PathVariable(value = "id") Long id){       
       
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try{         
            blogService.deleteById(id);
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");    
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }catch(Exception e){
            response.setStatus(false);
            response.setCode("500");
            response.setMessage( "id " + id + " not exists! " );
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
      
    }


}