package com.shaikhabdulgani.ConnectHub.service;

import com.shaikhabdulgani.ConnectHub.exception.CookieNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling cookies, including extraction, generation, and deletion.
 */
@Service
@RequiredArgsConstructor
public class CookieService {

    private static final String JWT_COOKIE_NAME = "token";
    private static final String USERID_COOKIE_NAME = "userId";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

//    @Value( "${backend.domain:localhost}" )
    private String domain = "localhost";

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
     * Extracts the value of the Refresh Token cookie from the provided HttpServletRequest.
     *
     * @param request The HttpServletRequest from which to extract the cookie.
     * @return The value of the Refresh Token cookie.
     * @throws CookieNotFoundException If the Refresh Token cookie is not found in the request.
     */
    public String extractRefreshTokenCookie(HttpServletRequest request) throws CookieNotFoundException {
        return extractCookie(request,REFRESH_TOKEN_COOKIE_NAME);
    }

    /**
     * Extracts the value of the user ID cookie from the provided HttpServletRequest.
     *
     * @param request The HttpServletRequest from which to extract the cookie.
     * @return The value of the user ID cookie.
     * @throws CookieNotFoundException If the user ID cookie is not found in the request.
     */
    public String extractUserIdCookie(HttpServletRequest request) throws CookieNotFoundException {
        return extractCookie(request,USERID_COOKIE_NAME);
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
        cookie.setAttribute("SameSite","None");
        cookie.setDomain(domain);
        return cookie;
    }

    /**
     * Generates a new Refresh Token cookie for the specified user.
     *
     * @param userId The user ID for which to generate the Refresh Token cookie.
     * @return The generated Refresh Token cookie.
     */
    public Cookie generateRefreshTokenCookie(String userId){
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME,refreshTokenService.generateRefreshToken(userId).getRefreshToken());
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite","None");
        cookie.setDomain(domain);
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
        cookie.setAttribute("SameSite","None");
        cookie.setDomain(domain);
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
        cookie.setSecure(true);
        cookie.setAttribute("SameSite","None");
        cookie.setDomain(domain);
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
     * Deletes the Refresh Token cookie by unsetting the Refresh Token cookie value to null and setting max age to 0.
     *
     * @return The deleted Refresh Token cookie.
     */
    public Cookie deleteRefreshTokenCookie(HttpServletRequest request){
        try {
            refreshTokenService.delete(extractRefreshTokenCookie(request));
        }catch (Exception ignored){}

        return deleteCookie(REFRESH_TOKEN_COOKIE_NAME);
    }

    /**
     * Deletes the user ID cookie by unsetting the user ID cookie value to null and setting max age to 0.
     *
     * @return The deleted user ID cookie.
     */
    public Cookie deleteUserIdCookie(){
        return deleteCookie(USERID_COOKIE_NAME);
    }

    public int indexOf(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i=0;i<cookies.length;i++) {
                if (cookies[i].getName().equals(cookieName)) {
                    return i;
                }
            }
        }
        return -1;
    }

}
