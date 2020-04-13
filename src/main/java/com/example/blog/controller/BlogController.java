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
import com.example.blog.common.dto.response.BaseResponseDTO;
import com.example.blog.common.dto.response.BlogResponse;
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
        @RequestParam(required = false, name="title") String title,
        @RequestParam(required = false, name="category_id") Long category_id,
        @RequestParam(required = false, name="author_id") Long author_id,
        @RequestParam(required = false, name="tags_name") Long tags_name, HttpServletRequest request
    ){

        Page<ResponseBlogDTO> blog;
        if(title != null){
            blog = blogService.findByTitle(MyPageable.convertToPageable(pageable),title); 
        }else if(category_id != null){
            blog = blogService.findByCategoriesId(MyPageable.convertToPageable(pageable),category_id); 
        }else if(author_id != null){
            blog = blogService.findByAuthorId(MyPageable.convertToPageable(pageable),author_id); 
        }else{
            blog = blogService.findAll(MyPageable.convertToPageable(pageable)); 
        }
        PageConverter<ResponseBlogDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/blog/",request.getScheme(),  request.getServerName(), request.getServerPort());
 
        if(title != null){
            search += "&title="+title;
        }else if(category_id != null){
            search += "&category_id="+category_id;
        }else{
            search += "&author_id="+author_id;
        }
 
        MyPage<ResponseBlogDTO> response = converter.convert(blog, url, search);
 
        return ResponseBaseDTO.ok(response);

    }

    @RequestMapping(value = "/blogs/{id}", method = RequestMethod.PUT)
    public ResponseBaseDTO updateBlog(@PathVariable Long id,@Valid @RequestBody BlogDTO request) {
        return ResponseBaseDTO.ok(blogService.update(id,request));
    }

    @RequestMapping(value = "/blogs/{id}", method = RequestMethod.GET)
    public ResponseBaseDTO<ResponseBlogDTO> getOne(@PathVariable Long id) {
        return ResponseBaseDTO.ok(blogService.findById(id));
    }

    @RequestMapping(value = "/blogs", method = RequestMethod.DELETE)
    public BaseResponseDTO<BlogResponse> deleteBlog(@RequestBody DeleteDTO request) {
        return blogService.delete(request);
    }


    


}
