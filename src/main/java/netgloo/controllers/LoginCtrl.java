package netgloo.controllers;

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
 * Created by vro on 04/11/16.
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
    public ResponseEntity<User> Login(@RequestBody LoginRequestParams params)
    {
        HttpHeaders corsHeader = setCors();
        HashMap<String, String> map = new HashMap<>();
        User user = LoginService.getByName(params.getUsername());

        if(user != null)
        {
            if(LoginService.comparePassword(user, params.getPassword()))
            {
                String token = generateToken(params);
                map.put("token", token);

                LoginService.Update(user);
                Cookie cookie = setCookie("token", token, 1);
                HttpServletResponse response = new HttpServletResponse;
                response.addCookie(cookie);


                user.setToken(token);
                user.setPassword("");
                user.setUsername("");

                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(user, corsHeader, HttpStatus.UNAUTHORIZED);
        }
        else
            return new ResponseEntity<>(user, HttpStatus.FORBIDDEN);
    }

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
