package netgloo.controllers;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.models.User;
import netgloo.services.BarService;
import netgloo.services.BeerService;
import netgloo.services.LoginService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vro on 08/10/16.
 */
@Controller
@RequestMapping("/bars")
public class BarCtrl extends ACtrl{

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Autowired
    private BarService barService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BeerService beerService;

    private boolean ActivateCookie = true;

    @RequestMapping(
            value = "",
            method = RequestMethod.GET)
    public ResponseEntity<Iterable<Bar>> GetAll(@RequestHeader HttpHeaders reqHeaders) {
        HttpHeaders headers = setCors();

        boolean cookieOk = checkCookie(reqHeaders.get("cookie"));
        Iterable<Bar> retList;
        HttpStatus retStatus;
        if(cookieOk)
        {
            retList = barService.all();
            retStatus = HttpStatus.CREATED;
        }
        else
        {
            retList = null;
            retStatus = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(retList, headers, retStatus);
    }

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
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> updateBar(@RequestBody Bar bar,
                                         @RequestHeader HttpHeaders reqHeaders) {
        final HttpHeaders corsHeader = setCors();

        boolean cookieOk = checkCookie(reqHeaders.get("cookie"));
        HttpStatus retStatus;
        Bar barUpdate;
        if(cookieOk) {
            barUpdate = barService.Update(bar);
            retStatus = HttpStatus.OK;
            if (barUpdate == null)
                retStatus = HttpStatus.NOT_FOUND;
        }
        else
        {
            barUpdate = null;
            retStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(barUpdate, corsHeader, retStatus);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> Create(@RequestBody Bar bar,
                                      @RequestHeader HttpHeaders reqHeaders) {
        HttpHeaders corsHeader = setCors();

        boolean cookieOk = checkCookie(reqHeaders.get("cookie"));
        HttpStatus retStatus;
        Bar response;
        if(cookieOk) {
            if (bar.getName().endsWith("\n"))
                bar.setName(bar.getName().substring(0, bar.getName().length() - 1));

            retStatus = HttpStatus.CREATED;
            response = barService.create(bar);
        }
        else
        {
            retStatus = HttpStatus.UNAUTHORIZED;
            response = null;
        }
        return new ResponseEntity<>(response, corsHeader, retStatus);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> deleteBar(@RequestBody Bar bar,
                                         @RequestHeader HttpHeaders reqHeaders) {
        HttpHeaders corsHeader = setCors();

        boolean cookieOk = checkCookie(reqHeaders.get("cookie"));
        HttpStatus retStatus;

        if(cookieOk) {
            retStatus = HttpStatus.OK;
            if (!barService.delete(bar.getName()))
                retStatus = HttpStatus.NOT_FOUND;
        }
        else
        {
            retStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(bar, corsHeader, retStatus);
    }

    @RequestMapping(
            value = "/{barname}/beers",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Beer> deleteBeer(@RequestBody Beer beer,
                                           @RequestHeader HttpHeaders reqHeaders) {
        List<Bar> listBars = beerService.getBarsWithThisBeer(beer.getName());
        HttpHeaders corsHeader = setCors();

        boolean cookieOk = checkCookie(reqHeaders.get("cookie"));
        HttpStatus retStatus;

        if(cookieOk) {
            retStatus = HttpStatus.OK;
            for (Bar bar : listBars) {
                barService.deleteBeerInBar(beer, bar);
            }
            if (!beerService.delete(beer))
                retStatus = HttpStatus.NOT_FOUND;
        }
        else
        {
            retStatus = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(beer, corsHeader, retStatus);
    }

    @RequestMapping(
            value = "/{barname}/beers",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody ResponseEntity<Bar> addBeerToBar(@RequestBody Beer beer,
                                                   @PathVariable("barname") String barname,
                                                   @RequestHeader HttpHeaders reqHeaders)
    {
        barname = barname.replace("+", " ");
        HttpHeaders corsHeader = setCors();

        boolean cookieOk = checkCookie(reqHeaders.get("cookie"));
        HttpStatus retStatus;
        Bar bar;
        if(cookieOk) {

            bar = barService.addBeerToBar(barname, beer, beerService);

            retStatus = HttpStatus.OK;

            if (bar == null)
                retStatus = HttpStatus.NOT_FOUND;
        }
        else
        {
            retStatus = HttpStatus.UNAUTHORIZED;
            bar = null;
        }
        return new ResponseEntity<>(bar, corsHeader, retStatus);
    }

    @RequestMapping(
            value = "/{barname}/beers",
            method = RequestMethod.OPTIONS)
    @ResponseBody
    public ResponseEntity ReplyOptionsBeer() {

        HttpHeaders corsHeader = setCors();
        return new ResponseEntity(null, corsHeader, HttpStatus.OK);
    }

    /**
     * Checks if the request's cookies contain the token, and verify this token with db
     * @param cookies
     * @return true if the token is ok
     */
    private boolean checkCookie(List<String> cookies) {

        if(!ActivateCookie)
            return true;

        if(cookies == null || cookies.size() == 0)
            return false;

        String[] cookiesStr = cookies.get(0).split(";");
        ArrayList<HttpCookie> coo = new ArrayList<>();

        for(String cooStr: cookiesStr) {
            List<HttpCookie> ll = HttpCookie.parse(cooStr.trim());

            if(ll.size() == 1)
            {
                coo.add(ll.get(0));
            }
            else
            {
                for(HttpCookie cook: ll)
                    coo.add(cook);
            }
        }

        int userId = 0,
                cooSize = coo == null
                        ? 0
                        : coo.size();
        String token = null;

        if(cooSize == 0)
            return false;

        for(int i = 0; i < cooSize; ++i)
        {
            String name = coo.get(i).getName();

            if(name.equals("token") )
            {
                token = coo.get(i).getValue();
            }
            else if(name.equals("id") )
            {
                userId = stringToInt( coo.get(i).getValue() );
            }
        }

        if(userId == 0 || token == null)
            return false;

        return checkToken(userId, token);
    }

    /**
     * Converts a string to int
     * @param value
     * @return the int obtained, null if format exception
     */
    private static int stringToInt(String value) {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException nfe)
        {
            return 0;
        }
    }

    /**
     * Check if token value is the same as db data for a selected user
     * @param userId the id of the user we want to match the token
     * @param tokenValue
     * @return true if the token matches
     */
    private boolean checkToken(int userId, String tokenValue) {
        // Get the user with his id
        User user = loginService.getById(userId);

        // Check if the user exist
        if (user == null)
            return false;

        // Check if his token correspond with the token received
        if (!tokenValue.equals(user.getToken()))
            return false;

        return true;
    }
}