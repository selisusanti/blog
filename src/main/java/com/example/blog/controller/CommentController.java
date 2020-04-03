package com.example.blog.controller;

import com.example.blog.repository.AuthorRepository;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CategoriesRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.AuthorService;
import com.example.blog.service.BlogService;
import com.example.blog.service.CategoriesService;
import com.example.blog.service.CommentService;

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

import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.model.Author;
import com.example.blog.model.Blog;
import com.example.blog.model.Categories;
import com.example.blog.model.Comment;
import com.example.blog.model.ResponseBaseDTO;

@RestController
@RequestMapping("/comments")
public class CommentController {


    @Autowired
    BlogRepository blogRepository;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> ListComment(Pageable pageable){
        ResponseBaseDTO response = new ResponseBaseDTO();         
        
        try
        {         
            Page<Comment> comment = commentService.findAll(pageable);
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(comment);         
            
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
            Optional<Comment> comment = commentService.findById(id); 
            if (comment.isPresent()) {           
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(comment);     
                
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
    public ResponseEntity<ResponseBaseDTO> updateBlog(@PathVariable("id") long id, @RequestBody Comment comment) throws NotFoundException{
       
        ResponseBaseDTO response    = new ResponseBaseDTO();
        Comment _comment            = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog id " + id + " NotFound"));
          
        try {
            
                _comment.setContent(comment.getContent());

                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");  
                response.setData(commentService.update(id, _comment));     

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
    public ResponseEntity<ResponseBaseDTO> createBlog(@RequestBody Comment comment) throws NotFoundException{
        
        // Blog result = new Blog();
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        Blog blog = blogRepository.findById(comment.getBlog_id()).orElseThrow(() -> new NotFoundException("Author id " + comment.getBlog_id() + " NotFound"));
       
        comment.setBlog(blog);
        
        try{
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(commentService.save(comment));           
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }catch(Exception e){
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public  ResponseEntity<ResponseBaseDTO> delete(@RequestBody DeleteDTO request) throws NotFoundException {       
       
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        Comment comment = commentRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Comment id " + request.getId() + " NotFound"));
            
        try{       
            commentRepository.delete(comment);
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");    
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }catch(Exception e){
            response.setStatus(false);
            response.setCode("500");
            response.setMessage( "id " + request.getId() + " not exists! " );
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
      
    }


}