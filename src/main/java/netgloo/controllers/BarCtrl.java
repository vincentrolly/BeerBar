package netgloo.controllers;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.services.BarService;
import netgloo.services.BeerService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class BarCtrl {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Autowired
    private BarService barService;

    @Autowired
    private BeerService beerService;


    @RequestMapping(
            value = "",
            method = RequestMethod.GET)
    public ResponseEntity<Iterable<Bar>> GetAll(@RequestHeader HttpHeaders reqHeaders) {
        HttpHeaders headers = new HttpHeaders();

        headers.put("Access-Control-Allow-Origin", Arrays.asList("http://localhost:3000"));
        headers.put("Access-Control-Allow-Credentials", Arrays.asList("true"));


        boolean cookieOk = this.checkCookie(reqHeaders.get("cookie"));
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

    private boolean checkCookie(List<String> cookies) {
        if(cookies == null || cookies.size() == 0)
            return false;

        String cookieStr = cookies.get(0);
        List<HttpCookie> coo = HttpCookie.parse(cookieStr);
        int cooSize = coo == null
            ? 0
            : coo.size();

        if(cooSize == 0)
            return false;

        for(int i = 0; i < cooSize; ++i)
        {
            String name = coo.get(i).getName();

            if(name.equals("token") )
            {
                String tokenValue = coo.get(i).getValue(),
                        // TODO get username from somewhere else
                        username = "myusername";

                return this.checkToken(username, tokenValue);
            }
        }

        //System.out.println(cookieStr);
        return false;
    }

    private boolean checkToken(String username, String tokenValue) {
        // TODO check in db
        return true;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> Create(@RequestBody String NameBar) {

        ResponseEntity<Bar> NewBar = new ResponseEntity<>(barService.create(NameBar), HttpStatus.CREATED);
        return NewBar;
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> updateBar(@RequestBody Bar bar)
    {
        if(barService.getByName(bar.getName()) != null)
        {
            barService.Update(bar);
            return new ResponseEntity<>(bar, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(bar, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> deleteBar(@RequestBody Bar bar)
    {
        if (barService.delete(bar.getName()))
            return new ResponseEntity<>(bar, HttpStatus.OK);
        else
            return new ResponseEntity<>(bar, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
            value = "/{barname}/beers",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Beer> deleteBeer(@RequestBody Beer beer)
    {
        List<Bar> listBars = beerService.getBarsWithThisBeer(beer.getName());

        for(Bar bar : listBars)
        {
            barService.deleteBeerInBar(beer, bar);
        }
        if(beerService.delete(beer))
            return new ResponseEntity<>(beer, HttpStatus.OK);
        else
            return new ResponseEntity<>(beer, HttpStatus.NOT_FOUND);
    }

}