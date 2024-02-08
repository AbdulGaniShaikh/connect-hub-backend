package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "images")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Image {

    @Id
    private String imageId;
    private String userId;
    private Binary image;
    private String title;
    private String imageType;
    private Date uploadDate;

}
