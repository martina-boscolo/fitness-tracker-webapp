package it.unipd.dei.cyclek.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUtils {

    public static Integer extractUserId(HttpServletRequest req) {
        Integer idUser = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    idUser = Integer.parseInt(TokenJWT.extractUserId(cookieValue));
                    break;
                }
            }
        }
        return idUser;
    }
}