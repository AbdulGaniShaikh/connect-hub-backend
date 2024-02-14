package com.shaikhabdulgani.ConnectHub.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a custom pageable response.
 * This class encapsulates information about pagination and the content of a page.
 *
 * @param <T> The type of content in the page
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPage<T> {

    /**
     * The number of elements in the current page.
     */
    private int size;

    /**
     * The page number of the current page.
     */
    private int pageNumber;

    /**
     * The size of the current page.
     */
    private int pageSize;

    /**
     * The total number of elements across all pages.
     */
    private int totalElements;

    /**
     * The total number of pages.
     */
    private int totalPages;

    /**
     * The number of elements in the current page.
     */
    private int numberOfElements;

    /**
     * Indicates whether the current page is empty.
     */
    private boolean empty;

    /**
     * The content of the current page.
     */
    private List<T> content;

    /**
     * Sets the size of the current page and updates the 'empty' flag accordingly.
     *
     * @param size The number of elements in the current page
     */
    public void setSize(int size) {
        this.size = size;
        this.empty = size == 0;
    }

    /**
     * Sets the total number of elements and calculates the total number of pages.
     *
     * @param totalElements The total number of elements across all pages
     */
    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
    }
}
