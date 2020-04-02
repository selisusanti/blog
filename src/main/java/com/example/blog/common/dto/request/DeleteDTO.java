package com.example.blog.common.dto.request;

import lombok.Data;

@Data
public class DeleteDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}