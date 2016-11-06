package netgloo.controllers;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import java.util.Arrays;

/**
 * Created by vro on 04/11/16.
 */
public class ACtrl
{
    /**
     *
     * @return HttpHeaders with parameters for cookies
     */
    protected HttpHeaders setCors()
    {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.put("Access-Control-Allow-Origin", Arrays.asList("https://davanture.fr:3000"));
        responseHeaders.put("Access-Control-Allow-Credentials", Arrays.asList("true"));
        return responseHeaders;
    }

    /**
     * Create a cookie with parameters
     * @param key : name of the key
     * @param value : value of the key
     * @param maxHours : max hours validity for the cookie created
     * @return cookie
     */
    protected static Cookie setCookie(String key, String value, int maxHours)
    {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxHours * 60 * 60);  // (s)
        cookie.setPath("/");

        return cookie;
    }
}
