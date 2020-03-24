package com.example.blog.controller;

import com.example.blog.service.CategoriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.blog.model.ResponseBaseDTO;

import java.util.List;
import java.util.Optional;

import com.example.blog.model.Categories;

@RestController
public class CategoriesController{

    @Autowired
    private CategoriesService categoriesService;

    @RequestMapping(value="/categories", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listUser(@RequestParam(required = false) String name){ 
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        try
        {         
            if(name == null){
                List<Categories> tagslist = categoriesService.findAll();
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tagslist);  
            }else{
                List<Categories> tagslist = categoriesService.findByName(name);
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


    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> getTagsById(@PathVariable("id") long id) {

        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try
        {     
            Optional<Categories> categories = categoriesService.findById(id); 
            if (categories.isPresent()) {           
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(categories);     
                
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



    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDTO> create(@RequestBody Categories categories){
        
        Categories result = new Categories();
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        if(categories.getName().isEmpty() )
        {
            response.setMessage("name is null");
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
        
        try{
            result =  categoriesService.save(categories);
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

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public  ResponseEntity<ResponseBaseDTO> delete(@PathVariable(value = "id") Long id){       
       
        ResponseBaseDTO response = new ResponseBaseDTO(); 

        try{         
            categoriesService.deleteById(id);
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

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseBaseDTO> updateUser(@PathVariable("id") long id, @RequestBody Categories categories) {
     
        ResponseBaseDTO response = new ResponseBaseDTO();
        try {
            Optional<Categories> categoriesData = categoriesService.findById(id);
            if (categoriesData.isPresent()) {
                Categories _categoriesData = categoriesData.get();
                _categoriesData.setName(categories.getName());

                // userData = userService.save(_user)
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");  
                response.setData(categoriesService.save(_categoriesData));            
                
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