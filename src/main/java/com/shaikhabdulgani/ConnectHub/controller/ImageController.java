package com.shaikhabdulgani.ConnectHub.controller;


import com.shaikhabdulgani.ConnectHub.dto.ApiResponse;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.model.Image;
import com.shaikhabdulgani.ConnectHub.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller class for handling image-related operations.
 */
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Retrieves an image by its ID.
     *
     * @param imageId The ID of the image to retrieve
     * @return A ResponseEntity containing the image data
     * @throws NotFoundException If the requested image is not found
     */
    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable String imageId) throws NotFoundException {
        Image image = imageService.getImage(imageId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(image.getImageType()))
                .body(image.getImage().getData());
    }

}
