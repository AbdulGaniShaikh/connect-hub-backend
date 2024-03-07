package com.shaikhabdulgani.ConnectHub.filter;

import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.NotFoundException;
import com.shaikhabdulgani.ConnectHub.exception.UnauthorizedAccessException;
import com.shaikhabdulgani.ConnectHub.model.User;
import com.shaikhabdulgani.ConnectHub.service.CookieService;
import com.shaikhabdulgani.ConnectHub.service.JwtService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import com.shaikhabdulgani.ConnectHub.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final BasicUserService userService;
    private final CookieService cookieService;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Optional<Cookie> cookie;
        int cookieIndex;
        if (!request.getRequestURI().startsWith("/ws") && !request.getRequestURI().substring("/api/auth/".length()).equals("/api/auth/") && request.getCookies()!=null) {
//            cookie = Arrays
//                    .stream(request.getCookies())
//                    .filter(cookie1 -> cookie1.getName().equals("token"))
//                    .findAny();
            cookieIndex = cookieService.indexOf(request,"token");


            String username = null;
            String token = null;
            if (cookieIndex!=-1) {
                Cookie cookie = request.getCookies()[cookieIndex];
                token = cookie.getValue();
                try {
                    username = jwtService.extractUsername(cookie.getValue());
                } catch (IllegalArgumentException e) {
                    logger.info("Illegal Argument while fetching the username !!");
                } catch (ExpiredJwtException e) {
                    logger.info("Given jwt token is expired !!");
                    try {
                        String[] info = generateAccessToken(request,response,cookieIndex);
                        token = info[0];
                        username = info[1];
                    }
                    catch (Exception ex){
                        logger.info(ex.getMessage());
                    }
                } catch (MalformedJwtException e) {
                    logger.info("Some changed has done in token !! Invalid Token");
                } catch (Exception e) {
                    logger.info(e.getMessage());
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

    public String[] generateAccessToken(HttpServletRequest request,HttpServletResponse response,int oldCookieIndex) throws CookieNotFoundException, NotFoundException, UnauthorizedAccessException {
        String refreshToken = cookieService.extractRefreshTokenCookie(request);
        String userId = cookieService.extractUserIdCookie(request);

        if (!refreshTokenService.validateToken(userId, refreshToken)){
            throw new UnauthorizedAccessException("Either refresh token is invalid or refresh token is expired.");
        }
        refreshTokenService.delete(refreshToken);
        User user = userService.getById(userId);

        Cookie newCookie = cookieService.generateJwtCookie(user.getUsername());
        request.getCookies()[oldCookieIndex].setValue(newCookie.getValue());
        response.addCookie(newCookie);
        response.addCookie(cookieService.generateRefreshTokenCookie(user.getUserId()));

        return new String[]{newCookie.getValue(),user.getUsername()};
    }
}
