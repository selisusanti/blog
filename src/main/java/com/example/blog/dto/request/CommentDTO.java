package com.example.blog.dto.request;

import lombok.Data;

@Data
public class CommentDTO {

    private Long id;
    private String guest_email;
    private String content;
    // private Integer blog;

}
