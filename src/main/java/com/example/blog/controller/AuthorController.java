package com.example.blog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.MyPage;
import com.example.blog.dto.MyPageable;
import com.example.blog.dto.request.AuthorDTO;
import com.example.blog.dto.request.AuthorPasswordDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.ResponseAuthorDTO;
import com.example.blog.dto.response.ResponseBaseDTO;
import com.example.blog.dto.util.PageConverter;
import com.example.blog.model.Author;
import com.example.blog.service.AuthorService;

@RestController
public class AuthorController {
    
    @Autowired
    AuthorService AuthorService;

    @RequestMapping(value = "/authors", method = RequestMethod.POST)
    public ResponseBaseDTO createAuthors(@Valid @RequestBody Author request) {
        return ResponseBaseDTO.ok(AuthorService.save(request));
    }

    @RequestMapping(value = "/authors/{id}", method = RequestMethod.GET)
    public ResponseBaseDTO<ResponseAuthorDTO> getOne(@PathVariable Long id) {
        return ResponseBaseDTO.ok(AuthorService.findById(id));
    }

    @RequestMapping(value = "/authors", method = RequestMethod.DELETE)
    public ResponseBaseDTO<ResponseAuthorDTO> deleteComment(@Valid @RequestBody DeleteDTO request) {
        return ResponseBaseDTO.ok(AuthorService.deleteById(request));
    }

    @RequestMapping(value = "/authors/{id}", method = RequestMethod.PUT)
    public ResponseBaseDTO updateAuthors(@PathVariable Long id,@Valid @RequestBody AuthorDTO request) {
        return ResponseBaseDTO.ok(AuthorService.update(id,request));
    }

    @RequestMapping(value = "/authors/{id}/password", method = RequestMethod.PUT)
    public ResponseBaseDTO updateAuthors(@PathVariable Long id,@Valid @RequestBody AuthorPasswordDTO request) {
        return ResponseBaseDTO.ok(AuthorService.updatePassword(id,request));
    }


    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public ResponseBaseDTO getAuthors(MyPageable pageable, 
        @RequestParam(required = false) String param, HttpServletRequest request
    ){

        Page<ResponseAuthorDTO> author;

        if(param != null){
            author = AuthorService.findByName(MyPageable.convertToPageable(pageable),param);
        }else{
            author = AuthorService.findAll(MyPageable.convertToPageable(pageable)); 
        }
        PageConverter<ResponseAuthorDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/authors/",request.getScheme(),  request.getServerName(), request.getServerPort());

 
        if(param != null){
            search += "&param="+param;
        }
 
        MyPage<ResponseAuthorDTO> response = converter.convert(author, url, search);
 
        return ResponseBaseDTO.ok(response);

    }

}