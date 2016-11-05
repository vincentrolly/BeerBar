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
import netgloo.models.IUserDao;
import netgloo.models.User;
import netgloo.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vro on 08/10/16.
 */
@Controller
@RequestMapping("/login")
public class LoginCtrl extends ACtrl
{
 // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Autowired
    private LoginService LoginService;

/*
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
*/

    @RequestMapping(
            value = "",
            method = RequestMethod.OPTIONS)
    @ResponseBody
    public ResponseEntity ReplyOptions() {

        HttpHeaders corsHeader = setCors();
        return new ResponseEntity(null, corsHeader, HttpStatus.OK);
    }


    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> Create(@RequestBody LoginRequestParams params, @RequestHeader HttpHeaders headersReq)
    {
        boolean ret = false;
        HttpStatus retStatus = HttpStatus.UNAUTHORIZED
        //JSONObject param = new JSONObject(jsonLoginParams);
        HttpHeaders headers = new HttpHeaders();


        //String test = param.getString("username");
        //String test = params.getUsername();
//        System.out.println(test);



        // check params
        String userLogin = params.getUsername(),
                userPass = params.getPassword();
        User user = this.checkCredentials(userLogin, userPass);
        ret = user != null;


        String token = null;

        if(ret == true)
        {
            //token = createToken();
            token = generateToken(params);
  
            this.storeToken(user, token);
            // TODO remove origin if unused
            String origin = headersReq.getOrigin();
            CookieHelper.addCookie(headers, token, origin);

//            response.setHeader("Access-Control-Allow-Origin", "*");
            headers.put("Access-Control-Allow-Origin", Arrays.asList("http://localhost:3000"));
            headers.put("Access-Control-Allow-Credentials", Arrays.asList("true"));

            retStatus = HttpStatus.OK;
        }

        user.setPassword("");
        user.setUsername("");
        if(token != null)
            user.setToken(token);

        ResponseEntity<User> NewBar = new ResponseEntity<>(user, headers, retStatus);
        return NewBar;
    }

    /**
     * Checks if a user exists in db, and checks if sumitted pass is correct
     * @param userLogin
     * @param userPass
     * @return the user credentials are ok, null otherwise
     */
    private User checkCredentials(String userLogin, String userPass) {
        //boolean ret = true;

        // TODO check userLogin and pass from db
        User user = LoginService.getByName(params.getUsername());

        if(user == null)
            return null;

        if(!LoginService.comparePassword(user, userPass))
            return null;



        return user;
    }

    /**
     * Adds token to the user in db
     * @param userLogin
     * @param token
     */
    private void storeToken(User user, String token) {
        // TODO sotre token for user in db
        user.setToken(token);
        LoginService.Update(user);
    }

    /**
     * Creates a token using current date, username
     * @return a sha256 encoded token
     */
    /*
    private String createToken() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String sdate = dateFormat.format(date); //2014/08/06 15:59:48

        // TODO add username and sha256 it

        return sdate;
    }
    */

    private String generateToken(LoginRequestParams param)
    {
        String str = new String(DatatypeConverter.parseBase64Binary(param.getUsername() + param.getPassword() + new Date()));
        String res = sha256(str);
        return res;
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


    public static class LoginRequestParams
    {
        String username;
        String password;

        public LoginRequestParams() {
        }

        public LoginRequestParams(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}