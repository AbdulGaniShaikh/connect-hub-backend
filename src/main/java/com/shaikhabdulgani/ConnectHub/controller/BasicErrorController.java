package com.shaikhabdulgani.ConnectHub.controller;

import com.shaikhabdulgani.ConnectHub.dto.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class BasicErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ApiError handleError(WebRequest request, HttpServletResponse response) {
        return new ApiError(HttpStatus.valueOf(response.getStatus()),errorAttributes.getError(request).getLocalizedMessage());
    }

}
