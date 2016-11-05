package netgloo.helpers;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    public static Cookie setCookie(String key, String value, int maxHours, String path)
    {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxHours * 60 * 60);  // (s)
//        cookie.setPath(path);
        cookie.setPath("/");
        cookie.setDomain("http://localhost:3000");

        return cookie;
    }

    /**
     * Adds our token cookie to the headers
     * @param headers reference to the headers
     * @param token token value for the cookie
     */
    public static void addCookie(HttpHeaders headers, String token, String origin)
    {
        // TODO Expires=

        Cookie cookie = setCookie("token", token, 1, origin);

        // set date for expires
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        long msDate = date.getTime();
        date.setTime( msDate + cookie.getMaxAge() * 1000 );
        String sdate = dateFormat.format(date);




        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; Path=" + cookie.getPath() + "; Max-Age=" + cookie.getMaxAge();
//        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; Domain=" + cookie.getDomain() + "; Path=" + cookie.getPath() + "; Max-Age=" + cookie.getMaxAge();
//        String cookieString = cookie.getName() + "=" + cookie.getValue() + "; Path=localhost; Expires=3600";// + sdate;

        headers.put("Set-Cookie", Arrays.asList(cookieString));
    }
}
