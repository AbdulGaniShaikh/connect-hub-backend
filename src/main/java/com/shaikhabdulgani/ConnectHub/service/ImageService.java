package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.model.Image;
import com.shaikhabdulgani.ConnectHub.repo.ImageRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private BasicUserService userService;

    public String uploadImage(MultipartFile file, String userId) throws IOException {

        Image image = Image.builder()
                .image(new Binary(BsonBinarySubType.BINARY,file.getBytes()))
                .imageType(file.getContentType())
                .userId(userId)
                .title(file.getOriginalFilename())
                .uploadDate(new Date())
                .build();

        image = imageRepo.save(image);
        return image.getImageId();
    }

    public Image getImage(String imageId) throws NotFoundException {
        Optional<Image> image = imageRepo.findById(imageId);
        if (image.isEmpty()){
            throw new NotFoundException("Image not found");
        }
        return image.get();
    }

}
