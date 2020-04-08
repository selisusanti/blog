package com.example.blog.service;

import com.example.blog.common.dto.request.DeleteDTO;
import com.example.blog.common.dto.response.ResponseAuthorDTO;
import com.example.blog.model.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService{

    ResponseAuthorDTO save(Author request);
    // Page<ResponseCommentDTO> findAllByBlogId(Pageable pageable, Long id);
    Page<ResponseAuthorDTO> findAll(Pageable pageable);
    Page<ResponseAuthorDTO> findByName(Pageable pageable, String param);
    ResponseAuthorDTO findById(Long id);
    ResponseAuthorDTO deleteById(DeleteDTO request);
    // ResponseCommentDTO deleteById(Long id);
    // void deleteAllByPostId (Long id);

    // private final AuthorRepository authorRepository;

    // @Autowired
    // public AuthorService(AuthorRepository authorRepository){
    //     this.authorRepository = authorRepository;
    // }

    // private ResponseAuthorDTO fromEntity(Author author) {
    //     ResponseAuthorDTO response = new ResponseAuthorDTO();
    //     BeanUtils.copyProperties(author, response);
    //     return response;
    // }

    // public Page<ResponseAuthorDTO> findAll(Pageable pageable){
    //     try {
    //         return authorRepository.findAll(pageable).map(this::fromEntity);
    //     } catch (Exception e) {
    //         log.error(e.getMessage(), e);
    //         throw e;
    //     }
    // }
 
    // public List<Author> findByName(String username){
    //     return authorRepository.findByUsername(username);
    // }

    // public Optional<Author> findById(Long id){
    //     return authorRepository.findById(id);
    // }

    // public Author save(Author author){
    //     return authorRepository.save(author);
    // }

    // public void deleteById(Long id) {
    //     authorRepository.deleteById(id);
    // }

}