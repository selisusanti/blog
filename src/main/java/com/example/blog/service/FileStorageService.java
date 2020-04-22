package com.example.blog.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.example.blog.config.FileStorageProperties;
import com.example.blog.dto.request.response.ResponseUploadFileDTO;
import com.example.blog.exception.FileStorageException;
import com.example.blog.exception.MyFileNotFoundException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Blog;
import com.example.blog.repository.BlogRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileStorageService {

    @Autowired
    private BlogRepository blogDao;

    private static final String RESOURCE = "Upload Blog";
    private static final String FIELD = "id";
    private final Path fileStorageLocation;

    private ResponseUploadFileDTO fromEntity(Blog blog) {
        ResponseUploadFileDTO response = new ResponseUploadFileDTO();
        BeanUtils.copyProperties(blog, response);
        return response;
    }

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public ResponseUploadFileDTO storeFile(MultipartFile file, Long id) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Blog blog = blogDao.findById(id).orElseThrow(() -> new ResourceNotFoundException(id.toString(), FIELD, RESOURCE));
           
          
           // Copy file to the target location (Replacing existing file with the same name)
           Path targetLocation = this.fileStorageLocation.resolve(fileName);
           Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

           String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
           .path("/api/downloadFile/")
           .path(fileName)
           .toUriString();

           blog.setImageUrl(fileDownloadUri);
        //    blog.setImage(file.getBytes());
           blogDao.save(blog);

           return fromEntity(blog);
        } catch (IOException ex) {
            // responseBaseDTO = new ResponseBaseDTO<ResponseUploadFileDTO>(500, false, "Failed upload image", null);
            // return new ResponseEntity<>(responseBaseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        //    return fromEntity(ex);
           throw new MyFileNotFoundException("Failed upload image " , ex);

        }
       
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}