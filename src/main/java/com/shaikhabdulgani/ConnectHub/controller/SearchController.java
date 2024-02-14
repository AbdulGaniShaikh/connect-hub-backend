package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.projection.UserProjection;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling user search operations.
 * This controller provides endpoints for searching users based on query parameters.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final BasicUserService basicUserService;

    /**
     * Searches for users based on the provided query.
     *
     * @param query The search query
     * @param pageNumber The page number for pagination (default value is 0)
     * @param pageSize The size of each page for pagination (default value is 10)
     * @return A Page object containing information about the users matching the search query
     * The page contains a subset of user information based on the pagination parameters.
     * Each page includes user details such as user ID, username, email and profile picture.
     */
    @GetMapping
    public Page<UserProjection> search(
            @RequestParam("q") String query,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize
    ){
        return basicUserService.searchUser(query,pageNumber,pageSize);
    }

}
