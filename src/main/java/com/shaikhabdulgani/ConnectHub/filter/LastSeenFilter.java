package com.shaikhabdulgani.ConnectHub.filter;

import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.JwtService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The `LastSeenFilter` class is a filter that updates the last seen timestamp of a user
 * whenever they make any request. It extends the Spring `OncePerRequestFilter`.
 */
public class LastSeenFilter extends OncePerRequestFilter {

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private JwtService jwtService;

    /**
     * Processes each incoming request to update the last seen timestamp of the user.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            basicUserService.updateLastSeen(cookieService.extractJwtCookie(request));
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}
