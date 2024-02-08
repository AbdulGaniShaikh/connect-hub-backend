package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling cookies, including extraction, generation, and deletion.
 */
@Service
public class CookieService {

    private static final String JWT_COOKIE_NAME = "token";
    private static final String USERID_COOKIE_NAME = "userId";


    @Autowired
    private JwtService jwtService;

    /**
     * Extracts the value of the specified cookie from the provided HttpServletRequest.
     *
     * @param request    The HttpServletRequest from which to extract the cookie.
     * @param cookieName The name of the cookie to extract.
     * @return The value of the specified cookie.
     * @throws CookieNotFoundException If the specified cookie is not found in the request.
     */
    public String extractCookie(HttpServletRequest request, String cookieName) throws CookieNotFoundException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        throw new CookieNotFoundException("Required cookie '" + cookieName + "' not found.");
    }

    /**
     * Extracts the value of the JWT (JSON Web Token) cookie from the provided HttpServletRequest.
     *
     * @param request The HttpServletRequest from which to extract the cookie.
     * @return The value of the JWT cookie.
     * @throws CookieNotFoundException If the JWT cookie is not found in the request.
     */
    public String extractJwtCookie(HttpServletRequest request) throws CookieNotFoundException {
        return extractCookie(request,JWT_COOKIE_NAME);
    }


    /**
     * Generates a new JWT cookie for the specified username.
     *
     * @param username The username for which to generate the JWT cookie.
     * @return The generated JWT cookie.
     */
    public Cookie generateJwtCookie(String username){
        Cookie cookie = new Cookie(JWT_COOKIE_NAME,jwtService.generateToken(username));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Generates a new user ID cookie for the specified user ID.
     *
     * @param userId The user ID for which to generate the user ID cookie.
     * @return The generated user ID cookie.
     */
    public Cookie generateUserIdCookie(String userId) {
        Cookie cookie = new Cookie(USERID_COOKIE_NAME,userId);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Deletes the specified cookie by unsetting the cookie value to null and setting max age to 0.
     *
     * @param name The name of the cookie to delete.
     * @return The deleted cookie.
     */
    public Cookie deleteCookie(String name){
        Cookie cookie = new Cookie(name,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Deletes the JWT cookie by unsetting the JWT cookie value to null and setting max age to 0.
     *
     * @return The deleted JWT cookie.
     */
    public Cookie deleteJwtCookie(){
        return deleteCookie(JWT_COOKIE_NAME);
    }

    /**
     * Deletes the user ID cookie by unsetting the user ID cookie value to null and setting max age to 0.
     *
     * @return The deleted user ID cookie.
     */
    public Cookie deleteUserIdCookie(){
        return deleteCookie(USERID_COOKIE_NAME);
    }

}
