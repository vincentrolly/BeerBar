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
public class BarCtrl extends ACtrl{

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
    public ResponseEntity<Bar> updateBar(@RequestBody Bar bar) {
        final HttpHeaders corsHeader = setCors();

        Bar barUpdate = barService.Update(bar);
        HttpStatus status = HttpStatus.OK;
        if (barUpdate == null)
            status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(barUpdate, corsHeader, status);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> Create(@RequestBody String NameBar) {
        HttpHeaders corsHeader = setCors();
        Bar response = barService.create(NameBar);

        return new ResponseEntity<>(response, corsHeader,  HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> deleteBar(@RequestBody Bar bar) {
        HttpHeaders corsHeader = setCors();
        if (barService.delete(bar.getName()))
            return new ResponseEntity<>(bar, corsHeader, HttpStatus.OK);
        else
            return new ResponseEntity<>(bar, corsHeader, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
            value = "/{barname}/beers",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Beer> deleteBeer(@RequestBody Beer beer) {
        List<Bar> listBars = beerService.getBarsWithThisBeer(beer.getName());
        HttpHeaders corsHeader = setCors();
        for (Bar bar : listBars) {
            barService.deleteBeerInBar(beer, bar);
        }
        if (beerService.delete(beer))
            return new ResponseEntity<>(beer, corsHeader, HttpStatus.OK);
        else
            return new ResponseEntity<>(beer, corsHeader, HttpStatus.NOT_FOUND);
    }

}