package com.shaikhabdulgani.ConnectHub.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageCountProject {
    private long count;
}
