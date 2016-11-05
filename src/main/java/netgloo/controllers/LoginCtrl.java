package netgloo.controllers;

import netgloo.helpers.CookieHelper;
import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.services.BarService;
import netgloo.services.BeerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by vro on 08/10/16.
 */
@Controller
@RequestMapping("/login")
public class LoginCtrl
{
//    @CrossOrigin(origins = "http://localhost:8080")
//    @RequestMapping(
//            value = "",
//            method = RequestMethod.GET)
//    public ResponseEntity<Iterable<Bar>> GetAll(HttpServletRequest request,
//                                                HttpServletResponse response) {
//        this.addCookie(response);
//        response.setHeader("Access-Control-Allow-Origin", "*");
//
//        return new ResponseEntity<>(barService.all(), HttpStatus.CREATED);
//    }

    @RequestMapping(
            value = "",
            method = RequestMethod.OPTIONS)
    @ResponseBody
    public ResponseEntity<Boolean> Create(HttpServletRequest request,
                                          HttpServletResponse response)
    {
        response.setHeader("Access-Control-Allow-Origin", "*");

        ResponseEntity<Boolean> NewBar = new ResponseEntity<>(true, HttpStatus.OK);
        return NewBar;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> Create(@RequestBody String jsonLoginParams, @RequestHeader HttpHeaders headersReq)
    {
        boolean ret = true;
        JSONObject param = new JSONObject(jsonLoginParams);
        HttpHeaders headers = new HttpHeaders();


        String test = param.getString("username");
//        System.out.println(test);



        // check params
        String userLogin = param.getString("username"),
                userPass = param.getString("password");
        ret = this.checkCredentials(userLogin, userPass);


        String token = null;

        if(ret == true)
        {
            token = createToken();

            this.storeToken(userLogin, token);
            String origin = headersReq.getOrigin();
            CookieHelper.addCookie(headers, token, origin);

//            response.setHeader("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Origin", Arrays.asList("http://localhost:3000"));
            headers.put("Access-Control-Allow-Credentials", Arrays.asList("true"));
        }

        if(token != null)
            param.putOnce("token", token);

        ResponseEntity<String> NewBar = new ResponseEntity<>(param.toString(), headers, HttpStatus.OK);
        return NewBar;
    }

    /**
     * Checks if a user exists in db, and checks if sumitted pass is correct
     * @param userLogin
     * @param userPass
     * @return true if credentials are ok
     */
    private boolean checkCredentials(String userLogin, String userPass) {
        boolean ret = true;

        // TODO check userLogin and pass from db

        return ret;
    }

    /**
     * Adds token to the user in db
     * @param userLogin
     * @param token
     */
    private void storeToken(String userLogin, String token) {
        // TODO sotre token for user in db
    }

    /**
     * Creates a token using current date, username
     * @return a sha256 encoded token
     */
    private String createToken() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String sdate = dateFormat.format(date); //2014/08/06 15:59:48

        // TODO add username and sha256 it

        return sdate;
    }




    /******************************/
    /**   OLD COOKIE HANDLINGT   **/
    /******************************/

//    /**
//     * Checks if the request contains the token cookie
//     * @param request
//     * @return true if the token is ok
//     */
//    private boolean checkCookie(HttpServletRequest request) {
//        boolean ret = true;
//
//        return ret;
//    }
//
//
//    /**
//     * Creates a cookie
//     * @param key the cookie's name
//     * @param value the cookie's value
//     * @param maxHours the cookie's duration in hour
//     * @return the crookie created
//     */
//    private static Cookie setCookie(String key, String value, int maxHours)
//    {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(maxHours * 60 * 60);  // (s)
//        cookie.setPath("/");
//
//        return cookie;
//    }
//
//    /**
//     * Adds our token cookie to the response
//     * @param response reference to the response
//     * @param token token value for the cookie
//     */
//    private void addCookie(HttpServletResponse response, String token)
//    {
//        Cookie cookie = setCookie("token", token, 1);
//        response.addCookie(cookie);   // response: reference to HttpServletResponse
//    }
//
    /******************************/
    /** END OLD COOKIE HANDLINGT **/
    /******************************/
}