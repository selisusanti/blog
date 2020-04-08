package com.example.blog.controller;

import com.example.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.blog.common.dto.CommentDTO;
import com.example.blog.common.dto.MyPage;
import com.example.blog.common.dto.MyPageable;
import com.example.blog.common.dto.response.ResponseBaseDTO;
import com.example.blog.common.dto.response.ResponseCommentDTO;
import com.example.blog.common.dto.util.PageConverter;

@RestController
public class CommentController {
    @Autowired
    CommentService CommentService;

    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.POST)
    public ResponseBaseDTO createComment(@PathVariable("id") long id,@Valid @RequestBody CommentDTO request) {
        return ResponseBaseDTO.ok(CommentService.save(request, id));
    }

    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET)
    public ResponseBaseDTO getComment(@PathVariable("id") long id,
        MyPageable pageable, @RequestParam(required = false) String param, HttpServletRequest request
    ){
        Page<ResponseCommentDTO> comments;

        if(param != null){
            comments = CommentService.findByName(MyPageable.convertToPageable(pageable),param);
        }else{
            comments = CommentService.findAllByBlogId(MyPageable.convertToPageable(pageable),id); 
           
        }
        PageConverter<ResponseCommentDTO> converter = new PageConverter<>();
       
        String search = ""; 
        String url = String.format("%s://%s:%d/posts/"+id+"/comment",request.getScheme(),  request.getServerName(), request.getServerPort());

 
        if(param != null){
            search += "&param="+param;
        }
 
        MyPage<ResponseCommentDTO> response = converter.convert(comments, url, search);
 
        return ResponseBaseDTO.ok(response);
 
    }

    @RequestMapping(value = "/posts/{blog}/comments/{id}", method = RequestMethod.GET)
    public ResponseBaseDTO<ResponseCommentDTO> getOneComment(@PathVariable Long blog, @PathVariable Long id) {
        return ResponseBaseDTO.ok(CommentService.findByBlogId(blog, id));
    }

    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.DELETE)
    public ResponseBaseDTO<ResponseCommentDTO> deleteComment(@PathVariable Long id) {
        return ResponseBaseDTO.ok(CommentService.deleteById(id));
    }

}