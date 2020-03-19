package com.example.blog.controller;

import java.util.List;

import com.example.blog.model.ResponseBaseDTO;
import com.example.blog.model.Tags;
import com.example.blog.repository.TagsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagsController{

    @Autowired
    private TagsRepository tagsRepository; 

    @RequestMapping(value="/tags", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listUser(){ 
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        try
        {         
            List<Tags> tagslist = tagsRepository.findAll();
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(tagslist);         
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }
        catch(Exception e)
        {
            // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
        }
        
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        
        // return userService.findAll();
    }


}