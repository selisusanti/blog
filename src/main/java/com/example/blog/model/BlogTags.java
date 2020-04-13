package com.example.blog.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "blog_tags")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt"},
        allowGetters = true
)
public class BlogTags implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "tags_id", nullable = false)

    @ManyToOne
    @JoinColumn(name = "tags_id")
    @JsonManagedReference
    private Tags tags;
    
    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "blog_id", nullable = false)
    @ManyToOne
    @JoinColumn(name = "blog_id")
    @JsonManagedReference
    private Blog blog;

    @JoinColumn(name = "created_at", updatable = false)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss",timezone="GMT+7")
    @CreationTimestamp
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    
}