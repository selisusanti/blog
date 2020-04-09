package com.example.blog.controller;

import com.example.blog.repository.BlogRepository;
import com.example.blog.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.common.dto.BlogDTO;
import com.example.blog.common.dto.MyPage;
import com.example.blog.common.dto.MyPageable;
import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.common.dto.response.ResponseBlogDTO;
import com.example.blog.common.dto.util.PageConverter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class BlogController {


    @Autowired
    BlogRepository blogRepository;

    @Autowired
    BlogService blogService;

    @RequestMapping(value = "/blogs", method = RequestMethod.POST)
    public ResponseBaseDTO createAuthors(@Valid @RequestBody BlogDTO request) {
        return ResponseBaseDTO.ok(blogService.save(request));
    }

    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public ResponseBaseDTO getAuthors(MyPageable pageable, 
        @RequestParam(required = false, name="title") String param,
        @RequestParam(required = false, name="category_id") Long category_id,
        @RequestParam(required = false, name="category_id") Long author_id,
        @RequestParam(required = false, name="tags_name") Long tags_name, HttpServletRequest request
    ){

        Page<ResponseBlogDTO> blog;

        blog = blogService.findAll(MyPageable.convertToPageable(pageable)); 
        PageConverter<ResponseBlogDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/blog/",request.getScheme(),  request.getServerName(), request.getServerPort());
 
        if(param != null){
            search += "&param="+param;
        }
 
        MyPage<ResponseBlogDTO> response = converter.convert(blog, url, search);
 
        return ResponseBaseDTO.ok(response);

    }

    @RequestMapping(value = "/blogs/{id}", method = RequestMethod.PUT)
    public ResponseBaseDTO updateBlog(@PathVariable Long id,@Valid @RequestBody BlogDTO request) {
        return ResponseBaseDTO.ok(blogService.update(id,request));
    }

    
}
