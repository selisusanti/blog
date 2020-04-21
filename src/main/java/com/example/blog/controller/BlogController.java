package com.example.blog.controller;

import com.example.blog.dto.MyPage;
import com.example.blog.dto.MyPageable;
import com.example.blog.dto.request.BlogDTO;
import com.example.blog.dto.request.DeleteDTO;
import com.example.blog.dto.response.BlogResponse;
import com.example.blog.dto.response.ResponseBaseDTO;
import com.example.blog.dto.response.ResponseBlogDTO;
import com.example.blog.dto.util.PageConverter;
import com.example.blog.repository.BlogRepository;
import com.example.blog.service.BlogService;
import com.example.blog.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class BlogController {


    @Autowired
    BlogRepository blogRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    BlogService blogService;

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @RequestMapping(value = "/blogs", method = RequestMethod.POST)
    public ResponseBaseDTO createAuthors(@Valid @RequestBody BlogDTO request) {
        return ResponseBaseDTO.ok(blogService.save(request));
    } 
    
    @RequestMapping(value = "/uploadFile/{id}", method = RequestMethod.POST)
    public ResponseBaseDTO uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
        return ResponseBaseDTO.ok(fileStorageService.storeFile(file, id));
    }

    @RequestMapping(value = "/downloadFile/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        System.out.println(fileName);
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        System.out.println(resource);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @RequestMapping(value = "/blogs", method = RequestMethod.GET)
    public ResponseBaseDTO getAuthors(MyPageable pageable, 
        @RequestParam(required = false, name="title") String title,
        @RequestParam(required = false, name="category_id") Long category_id,
        @RequestParam(required = false, name="author_id") Long author_id,
        @RequestParam(required = false, name="tag") String tag, HttpServletRequest request
    ){

        Page<ResponseBlogDTO> blog;
        if(title != null){
            blog = blogService.findByTitle(MyPageable.convertToPageable(pageable),title); 
        }else if(category_id != null){
            blog = blogService.findByCategoriesId(MyPageable.convertToPageable(pageable),category_id); 
        }else if(author_id != null){
            blog = blogService.findByAuthorId(MyPageable.convertToPageable(pageable),author_id); 
        }else if(tag != null){
            blog = blogService.findByTagsName(MyPageable.convertToPageable(pageable),tag); 
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
        }else if(tag != null){
            search += "&tag="+tag;
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
    public ResponseBaseDTO<BlogResponse> deleteBlog(@RequestBody DeleteDTO request) {
        return blogService.delete(request);
    }


    


}
