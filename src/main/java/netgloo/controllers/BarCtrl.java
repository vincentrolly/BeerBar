package netgloo.controllers;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.services.BarService;
import netgloo.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(
            value = "",
            method = RequestMethod.GET)
    public ResponseEntity<Iterable<Bar>> GetAll(HttpServletRequest request,
                                                HttpServletResponse response) {
        Cookie cookie = setCookie("token", "lolololo", 1);
        response.addCookie(cookie);   // response: reference to HttpServletResponse

        return new ResponseEntity<>(barService.all(), HttpStatus.CREATED);
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














//        @RequestMapping(
//            value = "/{nameBar}/add/{nameBeer}",
//            method = RequestMethod.POST)
//    public ResponseEntity<String> add(@PathVariable("nameBar") String nameBar,
//                                      @PathVariable("nameBeer") String nameBeer)
//    {
//        boolean beerFound = false;
//        for(Bar bar : barService.getAll())
//        {
//            if(bar.getName().equals(nameBar))
//            {
//                for(Beer beer : bar.getListBeer())
//                {
//                    if(beer.getName().equals(nameBeer))
//                        beerFound = true;
//                }
//                if(beerFound == false) {
//                    bar.getListBeer().add(beerService.getByName(nameBeer));
//                    barService.Update(bar);
//                    return new ResponseEntity<>("Bar Found !!! Add Beer OK !!!", HttpStatus.OK);
//                }
//                else
//                {
//                    return new ResponseEntity<>("Bar Found !!! Add Beer Unnecessary !!!", HttpStatus.OK);
//                }
//            }
//        }
//        return new ResponseEntity<>("Bar Not Found !!! Not add Beer !!!", HttpStatus.NOT_FOUND);
//    }

    private static Cookie setCookie(String key, String value, int maxHours)
    {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxHours * 60 * 60);  // (s)
        cookie.setPath("/");

        return cookie;
    }
}