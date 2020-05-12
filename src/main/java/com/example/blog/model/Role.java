
package com.example.blog.model;

import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    private String name;
    @ManyToMany(mappedBy = "roles")

    private Collection<Author> users;

}