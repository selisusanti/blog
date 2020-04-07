package com.example.blog.service;
import java.util.List;
import java.util.Optional;

import com.example.blog.model.Author;
import com.example.blog.repository.AuthorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService{
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public List<Author> findAll(){
        return authorRepository.findAll();
    }

    public List<Author> findByName(String username){
        return authorRepository.findByUsername(username);
    }

    public Optional<Author> findById(Long id){
        return authorRepository.findById(id);
    }

    public Author save(Author author){
        return authorRepository.save(author);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

}