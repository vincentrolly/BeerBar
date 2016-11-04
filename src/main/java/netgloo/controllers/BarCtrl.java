package netgloo.controllers;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.services.BarService;
import netgloo.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    //    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(
            value = "",
            method = RequestMethod.GET)
    public ResponseEntity<Iterable<Bar>> GetAll() {
        HttpHeaders corsHeader = setCors();
        ResponseEntity<Iterable<Bar>> titi = new ResponseEntity<>(barService.all(), corsHeader, HttpStatus.OK);
        return titi;
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

        ResponseEntity<Bar> NewBar = new ResponseEntity<>(barService.create(NameBar), HttpStatus.CREATED);
        return NewBar;
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

    private static HttpHeaders setCors()
    {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin","*");
        return responseHeaders;
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
}