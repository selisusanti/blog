package com.example.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

// import com.example.blog.model.ResponseBaseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.blog.common.dto.MyPage;
import com.example.blog.common.dto.MyPageable;
import com.example.blog.common.dto.response.ResponseAuthorDTO;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.common.dto.util.PageConverter;
import com.example.blog.model.Author;
import com.example.blog.repository.AuthorRepository;
import com.example.blog.service.AuthorService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;
    
    @Autowired
    AuthorService authorService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseBaseDTO<MyPage<ResponseAuthorDTO>> listAuthor(MyPageable pageable, HttpServletRequest request){
        Page<ResponseAuthorDTO> authlist;
        authlist = authorService.findAll(MyPageable.convertToPageable(pageable));
            
        PageConverter<ResponseAuthorDTO> converter = new PageConverter<>();
        String url = String.format("%s://%s:%d/authors",request.getScheme(),  request.getServerName(), request.getServerPort());

        String search = "";
        MyPage<ResponseAuthorDTO> outputdata = converter.convert(authlist, url, search);
        return ResponseBaseDTO.ok(outputdata);
        
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