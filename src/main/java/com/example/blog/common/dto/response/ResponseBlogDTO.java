package com.example.blog.common.dto.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.blog.model.Author;
import com.example.blog.model.Categories;
import com.example.blog.model.Tags;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data

public class ResponseBlogDTO {

    private Long id;
    private String title;
    private String content;
    private Author author;
    private Categories categories;
    private List<Tags> tag = new ArrayList<>();

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss",timezone="GMT+7")
    private Date created_at;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss",timezone="GMT+7")
    private Date updated_at; 
}