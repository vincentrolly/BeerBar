package netgloo.controllers;

import netgloo.helpers.CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

import netgloo.models.User;
import netgloo.services.LoginService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

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
    public ResponseEntity<User> Create(@RequestBody LoginRequestParams params)
    {
        boolean ret = false;
        HttpStatus retStatus = HttpStatus.UNAUTHORIZED;
        //JSONObject param = new JSONObject(jsonLoginParams);
        HttpHeaders headers = setCors();


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
            CookieHelper.addCookie(headers, token, user.getUserId());

//            response.setHeader("Access-Control-Allow-Origin", "*");


            retStatus = HttpStatus.OK;
        }
        else {
            user = new User();
            token = null;
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

        // on recupere le user par son login
        User user = LoginService.getByName(userLogin);

        // on verifie que le user existe
        if(user == null)
            return null;

        // on verifie que le pass correspond
        if(userPass.equals(user.getPassword()))
            return null;



        return user;
    }

    /**
     * Adds token to the user in db
     * @param user
     * @param token
     */
    private void storeToken(User user, String token) {
        user.setToken(token);
        LoginService.Update(user);
    }

    /**
     * Creates a token using current date, username
     * @param param param from body request
     * @return a sha256 encoded token
     */
    private String generateToken(LoginRequestParams param)
    {
        String str = new String(DatatypeConverter.parseBase64Binary(param.getUsername() + param.getPassword() + new Date()));
        String res = sha256(str);
        return res;
    }

    /**
     * Encode a string to sha256
     * @param base clear string
     * @return encoded string
     */
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


    /**
     * Model class for login parameters
     */
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