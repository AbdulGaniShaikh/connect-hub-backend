package com.shaikhabdulgani.ConnectHub.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
public class LastSeenProjection {

    private Date lastSeen;

}
