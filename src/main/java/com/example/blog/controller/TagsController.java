package com.example.blog.controller;

import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.model.ResponseBaseDTO;
import com.example.blog.model.Tags;
import com.example.blog.repository.TagsRepository;
import com.example.blog.service.TagsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
@RequestMapping("/tags")
public class TagsController{

    @Autowired
    private TagsService tagsService; 

    @Autowired
    private TagsRepository tagsRepository; 

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listTags(@RequestParam(required = false) String name, Pageable pageable){ 
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        try
        {         
            if(name == null){
                Page<Tags> tagslist = tagsService.findAll(pageable);
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tagslist);  
                  
                return new ResponseEntity<>(response ,HttpStatus.OK);
            }else{
                Page<Tags> tagslist = tagsService.findByNameContaining(name, pageable);
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tagslist);  
                  
                return new ResponseEntity<>(response ,HttpStatus.OK);
            }
        }
        catch(Exception e)
        {
            // catch error when get user
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
        }
        
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> getTagsById(@PathVariable("id") long id) {

        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try
        {     
            Optional<Tags> tags = tagsService.findById(id); 
            if (tags.isPresent()) {           
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tags);     
                
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
    public ResponseEntity<ResponseBaseDTO> create(@RequestBody Tags tags){
        
        Tags result = new Tags();
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        if(tags.getName().isEmpty() )
        {
            response.setMessage("name is null");
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
        
        try{

            Optional<Tags> detailList = tagsService.findByName(tags.getName());
            if (detailList.isPresent()) {
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(detailList.get());           
                return new ResponseEntity<>(response ,HttpStatus.OK);
            }else{
                result =  tagsService.save(tags);
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(result);           
                return new ResponseEntity<>(response ,HttpStatus.OK);
            }

        }catch(Exception e){
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public  ResponseEntity<ResponseBaseDTO> delete(@RequestBody DeleteDTO request){       
       
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try{         
            Tags tags = tagsRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Comment id " + request.getId() + " NotFound"));
            tagsRepository.delete(tags);
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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseBaseDTO> updateTags(@PathVariable("id") long id, @RequestBody User user) {
     
        ResponseBaseDTO response = new ResponseBaseDTO();
        try {
            Optional<Tags> tagsData = tagsService.findById(id);
            if (tagsData.isPresent()) {
                Tags _tags = tagsData.get();
                _tags.setName(user.getName());

                // userData = userService.save(_user)
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");  
                response.setData(tagsService.save(_tags));            
                
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


}