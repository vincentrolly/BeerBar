package netgloo.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vro on 04/11/16.
 */
public class ACtrl
{
    protected HttpHeaders setCors()
    {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin","*");
        return responseHeaders;
    }

    protected static Cookie setCookie(String key, String value, int maxHours)
    {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxHours * 60 * 60);  // (s)
        cookie.setPath("/");

        return cookie;
    }
}
