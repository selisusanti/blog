package com.example.blog.common.dto.response;
import lombok.Data;

@Data
public class ResponseUploadFileDTO {

    private String imageURL;
    private String type;
    private Long size;
    

}