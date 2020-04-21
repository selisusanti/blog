package com.example.blog.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.blog.dto.MyPage;
import com.example.blog.dto.MyPageable;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.request.TagsDTO;
import com.example.blog.dto.response.ResponseBaseDTO;
import com.example.blog.dto.response.ResponseTagsDTO;
import com.example.blog.dto.util.PageConverter;
import com.example.blog.repository.TagsRepository;
import com.example.blog.service.TagsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private TagsRepository tagsRepository; 


    @RequestMapping(value = "/tags/{id}", method = RequestMethod.GET)
    public ResponseBaseDTO<ResponseTagsDTO> getOne(@PathVariable Long id) {
        return ResponseBaseDTO.ok(tagsService.findById(id));
    }

    @RequestMapping(value = "/tags", method = RequestMethod.DELETE)
    public ResponseBaseDTO<ResponseTagsDTO> deleteTags(@Valid @RequestBody DeleteDTO request) {
        return ResponseBaseDTO.ok(tagsService.deleteById(request));
    }

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public ResponseBaseDTO createTags(@Valid @RequestBody TagsDTO request) {
        return ResponseBaseDTO.ok(tagsService.save(request));
    }

    @RequestMapping(value = "/tags/{id}", method = RequestMethod.PUT)
    public ResponseBaseDTO updateTags(@PathVariable Long id,@Valid @RequestBody TagsDTO request) {
        return ResponseBaseDTO.ok(tagsService.update(id,request));
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ResponseBaseDTO getTags(MyPageable pageable, 
        @RequestParam(required = false) String param, HttpServletRequest request
    ){

        Page<ResponseTagsDTO> tags;

        if(param != null){
            tags = tagsService.findByName(MyPageable.convertToPageable(pageable),param);
        }else{
            tags = tagsService.findAll(MyPageable.convertToPageable(pageable)); 
        }
        PageConverter<ResponseTagsDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/tags/",request.getScheme(),  request.getServerName(), request.getServerPort());

 
        if(param != null){
            search += "&param="+param;
        }
 
        MyPage<ResponseTagsDTO> response = converter.convert(tags, url, search);
 
        return ResponseBaseDTO.ok(response);

    }

}