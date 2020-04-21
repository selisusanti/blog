package com.example.blog.controller;

import com.example.blog.service.CategoriesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.repository.CategoriesRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.blog.dto.MyPage;
import com.example.blog.dto.MyPageable;
import com.example.blog.dto.request.CategoriesDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseBaseDTO;
import com.example.blog.dto.response.ResponseCategoriesDTO;
import com.example.blog.dto.util.PageConverter;

@RestController
public class CategoriesController{

    @Autowired
    private CategoriesService categoriesService;

 
    @Autowired
    private CategoriesRepository categoriesRepository;

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.GET)
    public ResponseBaseDTO<ResponseCategoriesDTO> getOne(@PathVariable Long id) {
        return ResponseBaseDTO.ok(categoriesService.findById(id));
    }

    @RequestMapping(value = "/categories", method = RequestMethod.DELETE)
    public ResponseBaseDTO<ResponseCategoriesDTO> deleteCategories(@Valid @RequestBody DeleteDTO request) {
        return ResponseBaseDTO.ok(categoriesService.deleteById(request));
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseBaseDTO createCategories(@Valid @RequestBody CategoriesDTO request) {
        return ResponseBaseDTO.ok(categoriesService.save(request));
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.PUT)
    public ResponseBaseDTO updateCategories(@PathVariable Long id,@Valid @RequestBody CategoriesDTO request) {
        return ResponseBaseDTO.ok(categoriesService.update(id,request));
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseBaseDTO getCategories(MyPageable pageable, 
        @RequestParam(required = false) String param, HttpServletRequest request
    ){

        Page<ResponseCategoriesDTO> categories;

        if(param != null){
            categories = categoriesService.findByName(MyPageable.convertToPageable(pageable),param);
        }else{
            categories = categoriesService.findAll(MyPageable.convertToPageable(pageable)); 
        }
        PageConverter<ResponseCategoriesDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/categories/",request.getScheme(),  request.getServerName(), request.getServerPort());

 
        if(param != null){
            search += "&param="+param;
        }
 
        MyPage<ResponseCategoriesDTO> response = converter.convert(categories, url, search);
 
        return ResponseBaseDTO.ok(response);

    }

}