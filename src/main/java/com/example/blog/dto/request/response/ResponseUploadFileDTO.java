package com.example.blog.dto.request.response;
import lombok.Data;

@Data
public class ResponseUploadFileDTO {

    private String imageURL;
    private String type;
    private Long size;
    

}