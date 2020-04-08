package com.example.blog.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import com.example.blog.common.dto.CommentDTO;
import com.example.blog.common.dto.exception.ResourceNotFoundException;
import com.example.blog.common.dto.response.ResponseCommentDTO;
import com.example.blog.model.Blog;
import com.example.blog.model.Comment;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.CommentService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentServiceImp implements CommentService{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentService commentService;
    
    private static final String RESOURCE = "Comment";
    private static final String FIELD = "id";

    private ResponseCommentDTO fromEntity(Comment comment) {
        ResponseCommentDTO response = new ResponseCommentDTO();
        BeanUtils.copyProperties(comment, response);
        return response;
    }

    @Override
    public Page<ResponseCommentDTO> findAllByBlogId(Pageable pageable, Long id) {
        try {
            Blog blog = blogRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            return commentRepository.findAllByBlogId(pageable, id).map(this::fromEntity);
        } 
        catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Page<ResponseCommentDTO> findByName(Pageable pageable, String param) {
        try {
            param = param.toLowerCase();
            return commentRepository.findByName(pageable, param).map(this::fromEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public ResponseCommentDTO save(CommentDTO request, Long id) {
        try {
            Comment comment = new Comment();

            Blog blog = blogRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Blog not found."));

            comment.setGuest_email(request.getGuest_email());
            comment.setContent(request.getContent());
            comment.setBlog(blog);
            comment.setCreated_at(new Date());
            comment.setUpdated_at(new Date());
            
            commentRepository.save(comment);
            return fromEntity(comment);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseCommentDTO findByBlogId(Long blog, Long id) {
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
            
            Comment res = commentRepository.findByBlogId(blog, id);

            return fromEntity(res);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseCommentDTO deleteById(Long id) {
        try {
            ResponseCommentDTO response = new ResponseCommentDTO();

            Blog blog = blogRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
           
            commentService.deleteAllByPostId(blog.getId());
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public void deleteAllByPostId(Long id) {
        try {
            commentRepository.deleteAllPostByID(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
            
    }

}