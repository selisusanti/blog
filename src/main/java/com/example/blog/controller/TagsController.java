package com.example.blog.controller;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.ResponseBaseDTO;
import com.example.blog.model.Tags;
import com.example.blog.service.TagsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagsController{

    @Autowired
    private TagsService tagsService; 

    @RequestMapping(value="/tags", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listUser(@RequestParam(required = false) String name){ 
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        try
        {         
            if(name == null){
                List<Tags> tagslist = tagsService.findAll();
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tagslist);  
            }else{
                List<Tags> tagslist = tagsService.findByName(name);
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tagslist);  
            }
                  
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
    }

    @RequestMapping(value = "/tags/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDTO> create(@RequestBody Tags tags){
        
        Tags result = new Tags();
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        if(tags.getName().isEmpty() )
        {
            response.setMessage("name is null");
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
        
        try{
            result =  tagsService.save(tags);
            response.setStatus(true);
            response.setCode("200");
            response.setMessage("success");
            response.setData(result);           
            return new ResponseEntity<>(response ,HttpStatus.OK);
        }catch(Exception e){
            response.setStatus(false);
            response.setCode("500");
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
       
    }

    @RequestMapping(value = "/tags/{id}", method = RequestMethod.DELETE)
    public  ResponseEntity<ResponseBaseDTO> delete(@PathVariable(value = "id") Long id){       
       
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try{         
            tagsService.deleteById(id);
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

    @RequestMapping(value = "/tags/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseBaseDTO> updateUser(@PathVariable("id") long id, @RequestBody User user) {
     
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