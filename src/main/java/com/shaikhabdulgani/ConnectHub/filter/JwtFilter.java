package com.shaikhabdulgani.ConnectHub.filter;

import com.shaikhabdulgani.ConnectHub.service.JwtService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BasicUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> cookie = Optional.empty();
        if (!request.getRequestURI().startsWith("/ws") && !request.getRequestURI().substring("/api/auth/".length()).equals("/api/auth/") && request.getCookies()!=null) {
            cookie = Arrays
                    .stream(request.getCookies())
                    .filter(cookie1 -> cookie1.getName().equals("token"))
                    .findAny();


            String username = null;
            String token = null;
            if (cookie.isPresent()) {
                token = cookie.get().getValue();
                try {
                    username = jwtService.extractUsername(cookie.get().getValue());
                } catch (IllegalArgumentException e) {
                    logger.info("Illegal Argument while fetching the username !!");
                    e.printStackTrace();
                } catch (ExpiredJwtException e) {
                    logger.info("Given jwt token is expired !!");
                    e.printStackTrace();
                } catch (MalformedJwtException e) {
                    logger.info("Some changed has done in token !! Invalid Token");
                    e.printStackTrace();
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.info("Cant Validate");
                }
            }
        }

        filterChain.doFilter(request,response);
    }
}
