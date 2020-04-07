package com.example.blog.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

// import com.example.blog.model.ResponseBaseDTO;
import com.example.blog.repository.CategoriesRepository;

import java.util.List;
import java.util.Optional;

import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.model.Categories;

@RestController
@RequestMapping("/categories")
public class CategoriesController{

    @Autowired
    private CategoriesService categoriesService;


    @Autowired
    private CategoriesRepository categoriesRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> listCategories(@RequestParam(required = false) String name, Pageable pageable){ 
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        try
        {         
            if(name == null){
                Page<Categories> tagslist = categoriesService.findAll(pageable);
                response.setStatus(true);
                response.setCode("200");
                response.setMessage("success");
                response.setData(tagslist);  
            }else{
                Page<Categories> tagslist = categoriesService.findByNameContaining(name,pageable);
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDTO> getCategoriesById(@PathVariable Long id) throws NotFoundException {
        ResponseBaseDTO response = new ResponseBaseDTO<>();

        Categories categories = categoriesRepository.findById(id).orElseThrow(() -> new NotFoundException("Categories id " + id + " NotFound"));

        response.setData(categories);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDTO> createCategories(@RequestBody Categories categories){
        
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

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public  ResponseEntity<ResponseBaseDTO> delete(@RequestBody DeleteDTO request){       
       
        ResponseBaseDTO response = new ResponseBaseDTO(); 
        
        try{     
            Categories categories = categoriesRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Comment id " + request.getId() + " NotFound"));
            categoriesRepository.delete(categories);
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
    public ResponseEntity<ResponseBaseDTO> updateCategories(@PathVariable("id") long id, @RequestBody Categories categories) {
     
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