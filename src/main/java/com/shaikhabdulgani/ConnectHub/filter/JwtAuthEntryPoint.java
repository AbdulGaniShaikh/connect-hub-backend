package com.shaikhabdulgani.ConnectHub.filter;

import com.shaikhabdulgani.ConnectHub.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String errorMessage = authException.getMessage().equals("Bad credentials") ? "Username and password doesn't match" : "Token not valid";
        ApiError error = new ApiError(HttpStatus.valueOf(response.getStatus()) , errorMessage,true);
        response.setContentType("application/json");
        response.getWriter().println(new JSONObject(error));
    }
}
