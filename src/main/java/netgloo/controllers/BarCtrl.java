package netgloo.controllers;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.services.BarService;
import netgloo.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vro on 08/10/16.
 */
@Controller
@RequestMapping("/bars")
public class BarCtrl
{
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
    public ResponseEntity<Iterable<Bar>> all(HttpEntity<byte[]> requestEntity)
    {
        String requestHeader = requestEntity.getHeaders().getFirst("MyRequestHeader");
        byte[] requestBody = requestEntity.getBody();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(barService.all(), responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/{nameBar}",
            method = RequestMethod.GET)
    public ResponseEntity<Bar> one(@PathVariable("nameBar") String nameBar)
    {
        Bar bar = barService.getByName(nameBar);
        if(bar != null)
        {
            return new ResponseEntity<>(bar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> create(@RequestBody Bar bar)
    {
        if(barService.create(bar))
        {
            return new ResponseEntity<>(bar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping(
            value = "/update/{nameBar}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Bar> update(@RequestBody Bar bar, @PathVariable("nameBar") String nameBar)
    {
        Bar barToUpdate = barService.getByName(nameBar);
        bar.setBarId(barToUpdate.getBarId());
        if(barService.update(bar))
        {
            return new ResponseEntity<>(bar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping(
            value = "/delete/{nameBar}",
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable("nameBar") String nameBar)
    {
//        beerService.deleteAllBeer();
        if(barService.delete(nameBar))
        {
            return new ResponseEntity<>("Bar Found !!! Deleted !!!", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Bar Not Found !!! Not deleted !!!", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(
            value = "/{nameBar}/add/{nameBeer}",
            method = RequestMethod.POST)
    public ResponseEntity<String> add(@PathVariable("nameBar") String nameBar,
                                      @PathVariable("nameBeer") String nameBeer)
    {
        boolean beerFound = false;
        for(Bar bar : barService.getAll())
        {
            if(bar.getName().equals(nameBar))
            {
                for(Beer beer : bar.getListBeer())
                {
                    if(beer.getName().equals(nameBeer))
                        beerFound = true;
                }
                if(beerFound == false) {
                    bar.getListBeer().add(beerService.getByName(nameBeer));
                    barService.update(bar);
                    return new ResponseEntity<>("Bar Found !!! Add Beer OK !!!", HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>("Bar Found !!! Add Beer Unnecessary !!!", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Bar Not Found !!! Not add Beer !!!", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
            value="/{nameBar}/remove/{nameBeer}",
            method = RequestMethod.POST)
    public ResponseEntity<String> remove(@PathVariable("nameBar") String nameBar,
                                         @PathVariable("nameBeer") String nameBeer)
    {
        for(Bar bar : barService.getAll())
        {
            if(bar.getName().equals(nameBar))
            {
                for(Beer beer : bar.getListBeer())
                {
                    if(beer.getName().equals(nameBeer))
                    {
                        bar.getListBeer().remove(beerService.getByName(nameBeer));
                        barService.update(bar);
                        return new ResponseEntity<>("Bar Found !!! Remove Beer OK !!!", HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>("Bar Found !!! Not Remove Beer !!!", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>("Bar Not Found !!! Not remove Beer !!!", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Failed !!!", HttpStatus.NOT_ACCEPTABLE);
    }
}
