package com.example.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

import com.example.blog.model.ResponseBaseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.blog.model.Author;
import com.example.blog.repository.AuthorRepository;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listAuthor(){
        ResponseBaseDTO response = new ResponseBaseDTO();         
     
        try
        {         
            List<Author> tags = authorRepository.findAll();
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(tags);         
            
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


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDTO> create(@RequestBody Author authors){
         
        Author resultaAuthor = new Author();       
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try
        {         
            authors.setPassword(passwordEncoder().encode(authors.getPassword()));  
            resultaAuthor =  authorRepository.save(authors);
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(resultaAuthor);           
            
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


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseBaseDTO> updateAuthor(@PathVariable("id") long id, @RequestBody Author authors) {
       
        ResponseBaseDTO response = new ResponseBaseDTO();

        try {
            Optional<Author> authorData = authorRepository.findById(id);
            if (authorData.isPresent()) {
                Author _author = authorData.get();
                _author.setFirst_name(authors.getFirst_name());
                _author.setLast_name(authors.getLast_name());
                _author.setUsername(authors.getUsername());
             
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");  
                response.setData(authorRepository.save(_author));            
                
            }
            return new ResponseEntity<>( response, HttpStatus.OK);
          
        } catch (Exception e) {
            // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage( "id " + id + " not exists! " );
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }
    @RequestMapping(value = "/updatepassword/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseBaseDTO> updatePassword(@PathVariable("id") long id, @RequestBody Author authors) {
       
        ResponseBaseDTO response = new ResponseBaseDTO();

        try {
            Optional<Author> authorData = authorRepository.findById(id);
            if (authorData.isPresent()) {
                Author _author = authorData.get();
                _author.setPassword(passwordEncoder().encode(authors.getPassword()));  
             
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");  
                response.setData(authorRepository.save(_author));            
                
            }
            return new ResponseEntity<>( response, HttpStatus.OK);
          
        } catch (Exception e) {
            // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage( "id " + id + " not exists! " );
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> getAuthorById(@PathVariable Long id) throws NotFoundException {
        ResponseBaseDTO response = new ResponseBaseDTO<>();

        Author authors = authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Categories id " + id + " NotFound"));

        response.setData(authors);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}