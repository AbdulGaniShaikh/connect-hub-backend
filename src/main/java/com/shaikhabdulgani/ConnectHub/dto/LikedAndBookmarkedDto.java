package com.shaikhabdulgani.ConnectHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the liked and bookmarked status of an entity.
 * This class encapsulates whether an entity is liked and/or bookmarked.
 */
@Data
@AllArgsConstructor
public class LikedAndBookmarkedDto {

    /**
     * Indicates whether the entity is liked.
     */
    private boolean isLiked;

    /**
     * Indicates whether the entity is bookmarked.
     */
    private boolean isSaved;

}
