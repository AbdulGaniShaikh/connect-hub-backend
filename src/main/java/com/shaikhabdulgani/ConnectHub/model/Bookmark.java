package com.shaikhabdulgani.ConnectHub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "bookmarks")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Bookmark {

    @Id
    private String bookmarkId;
    private String postId;
    private String userId;
    private Date date;

}
