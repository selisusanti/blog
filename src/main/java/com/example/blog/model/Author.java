package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "author")
public class Author extends AuditModel{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(length = 45)
    private String username;
    @Column(length = 150)
    private String password;
    @Column(length = 45)
    private String first_name;
    @Column(length = 45)
    private String last_name;

    @ManyToMany
    @JoinTable(
        name = "author_role", 
        joinColumns = { @JoinColumn(name = "author_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id")}
    )
    
    private List<Role> roles;


    
}