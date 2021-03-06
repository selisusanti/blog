package com.example.blog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "blog")
//ini untuk jika ada array
public class Blog extends AuditModel implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private transient Long author_id;

    private transient Long categories_id;

    private transient List<Long> tags_id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categories_id", nullable = false)
    private Categories categories;

    @Size(max = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "blog_tags", 
        joinColumns = { @JoinColumn(name = "blog_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "tags_id")}
    )
    private List<Tags> tag = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }

    public Long getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(Long categories_id) {
        this.categories_id = categories_id;
    }

    public List<Long> getTags_id() {
        return tags_id;
    }

    public void setTags_id(List<Long> tags_id) {
        this.tags_id = tags_id;
    }

    public List<Tags> getTag() {
        return tag;
    }

    public void setTag(List<Tags> tag) {
        this.tag = tag;
    }

    

    


}