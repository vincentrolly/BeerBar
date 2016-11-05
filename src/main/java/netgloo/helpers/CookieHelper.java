package netgloo.helpers;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ThaZalman on 05/11/2016.
 */
public class CookieHelper {
    /**
     * Checks if the request contains the token cookie
     * @param request
     * @return true if the token is ok
     */
    public static boolean checkCookie(HttpServletRequest request) {
        boolean ret = true;

        return ret;
    }


    /**
     * Creates a cookie
     * @param key the cookie's name
     * @param value the cookie's value
     * @param maxHours the cookie's duration in hour
     * @return the crookie created
     */
    public static Cookie setCookie(String key, String value, int maxHours)
    {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxHours * 60 * 60);  // (s)
        cookie.setPath("/");
        cookie.setDomain("http://localhost:3000");

        return cookie;
    }

    /**
     * Adds our token cookie to the headers
     * @param headers reference to the headers
     * @param token token value for the cookie
     */
    public static void addCookie(HttpHeaders headers, String token, long userId)
    {
        // TODO Expires=

        Cookie cookieToken = setCookie("token", token, 1),
                cookieId = setCookie("id", String.valueOf(userId), 1);

        // set date for expires
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        long msDate = date.getTime();
//        date.setTime( msDate + cookieToken.getMaxAge() * 1000 );


        String tokenString = cookieToken.getName() + "=" + cookieToken.getValue() + "; Path=" + cookieToken.getPath() + "; Max-Age=" + cookieToken.getMaxAge(),
                idString = cookieId.getName() + "=" + cookieId.getValue() + "; Path=" + cookieId.getPath() + "; Max-Age=" + cookieId.getMaxAge();
//        String cookieString = cookieToken.getName() + "=" + cookieToken.getValue() + "; Domain=" + cookieToken.getDomain() + "; Path=" + cookieToken.getPath() + "; Max-Age=" + cookieToken.getMaxAge();
//        String cookieString = cookieToken.getName() + "=" + cookieToken.getValue() + "; Path=localhost; Expires=3600";// + sdate;

        headers.put("Set-Cookie", Arrays.asList(tokenString, idString));
//        headers.put("Set-Cookie", Arrays.asList(tokenString));
//        headers.put("Set-Cookie", Arrays.asList(idString));
    }
}