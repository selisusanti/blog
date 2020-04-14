package com.example.blog.common.dto;

import java.util.List;

// import com.example.blog.model.Author;
// import com.example.blog.model.Categories;

import lombok.Data;
@Data
public class BlogDTO {

    private Integer id;
    private String title;
    private String content;

    private transient Long author_id;
    private transient Long categories_id;
    private transient List<String> tags_name;
    // private Author author;
    // private Categories categories;
    // private String[] tags;


}
