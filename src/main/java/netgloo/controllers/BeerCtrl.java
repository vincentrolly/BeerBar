package netgloo.controllers;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.models.IBeerDao;
import netgloo.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by vro on 08/10/16.
 */
@Controller
@CrossOrigin(maxAge = 3600)
@RequestMapping("/beers")
public class BeerCtrl {
    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerService barService;


    @RequestMapping(
            value = "",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Beer> UpdateAll(@RequestBody Beer beer) {

        return new ResponseEntity<>(beerService.Update(beer), HttpStatus.OK);
    }
















//    @RequestMapping(
//            value = "",
//            method = RequestMethod.GET)
//    public ResponseEntity<Iterable<Beer>> GetAll() {
//        return new ResponseEntity<>(beerService.all(), HttpStatus.OK);
//    }
//
//    @RequestMapping(
//            value = "",
//            method = RequestMethod.DELETE)
//    public ResponseEntity<String> DeleteAll() {
//        return new ResponseEntity<>(beerService.deleteAllBeer(), HttpStatus.OK);
//    }
//    @RequestMapping(
//            value = "",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> UpdateAll(@RequestBody Beer beer) {
//        return new ResponseEntity<>(beerService.UpdateAll(beer), HttpStatus.OK);
//    }
//
//    @RequestMapping(
//            value = "/{nameBeer}",
//            method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Beer> GetOneBeer(@PathVariable("nameBeer") String nameBeer) {
//        Beer beer = beerService.getByName(nameBeer);
//        if (beer != null) {
//            return new ResponseEntity<>(beer, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @RequestMapping(
//            value = "/{nameBeer}",
//            method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<Beer> UpdateOneBeer(@RequestBody Beer beer) {
//        Beer beer = beerService.getByName(beer.getName());
//        if (beer != null)
//        {
//            if (beerService.update(beer))
//            {
//                return new ResponseEntity<>(beer, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @RequestMapping(
//            value = "/{nameBeer}",
//            method = RequestMethod.DELETE)
//    @ResponseBody
//    public ResponseEntity<Beer> DeleteOneBeer(@PathVariable("nameBeer") String nameBeer) {
//        Beer beer = beerService.getByName(nameBeer);
//        if (beer != null) {
//            if(beerService.delete(beer))
//                return new ResponseEntity<>(beer, HttpStatus.OK);
//            else
//                return new ResponseEntity<>(beer, HttpStatus.NOT_ACCEPTABLE);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @RequestMapping(
//            value = "/getBars/{nameBar}",
//            method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<List<Beer>> GetBeersFromBar(@RequestBody Bar bar) {
//        if (bar)
//        {
//            if(bar.getBarId() > -1)
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
//
//        }
//    }    @RequestMapping(
//            value = "/getBars/{nameBar}",
//            method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<List<Beer>> GetBeersFromBar(@RequestBody Bar bar) {
//        if (bar)
//        {
//            if(bar.getBarId() > -1)
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
//
//        }
//    }
//    @RequestMapping(
//            value = "/getBars/{nameBar}",
//            method = RequestMethod.PUT)
//    @RequestMapping(
//            value = "/getBars/{nameBar}",
//            method = RequestMethod.DELETE)
//
//    @RequestMapping(
//            value = "/create",
//            method = RequestMethod.POST)
//
//    @ResponseBody
//    public ResponseEntity<Beer> create(@RequestBody Beer beer) {
//        if (beerService.create(beer)) {
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//    }
//
//
//    @RequestMapping(
//            value = "/update/{id}",
//            method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<Beer> update(@RequestBody Beer beer, @PathVariable("id") long id) {
//        beer.setBeerId(id);
//        if (beerService.update(beer)) {
//            return new ResponseEntity<>(beer, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(beer, HttpStatus.NOT_ACCEPTABLE);
//    }
//
//    @RequestMapping(
//            value = "/update/{nameBeer}",
//            method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<Beer> update(@RequestBody Beer beer, @PathVariable("nameBeer") String nameBeer)
//    {
//        Beer beerToUpdate = beerService.getByName(nameBeer);
//        beer.setBeerId(beerToUpdate.getBeerId());
//        if(beerService.update(beer))
//        {
//            return new ResponseEntity<>(beer, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//    }
//
//
//    @RequestMapping(
//            value = "/delete/{nameBeer}",
//            method = RequestMethod.DELETE
//    )
//    @ResponseBody
//    public ResponseEntity<String> delete(@PathVariable("nameBeer") String nameBeer)
//    {
//        if(beerService.delete(nameBeer))
//        {
//            return new ResponseEntity<>("Beer Found !!! Deleted !!!", HttpStatus.OK);
//        }
//        else
//        {
//            return new ResponseEntity<>("Beer Not Found !!! Not deleted !!!", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @RequestMapping(
//            value = "/delete/{nameBeer}/{degree}",
//            method = RequestMethod.DELETE
//    )
//    @ResponseBody
//    public ResponseEntity<Beer> delete_with_degree(@PathVariable("nameBeer") String nameBeer,
//                                                   @PathVariable("degree") long degree) {
//        List<Beer> toto = beerService.getAllByName(nameBeer);
//        ResponseEntity<Beer> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        for (Beer b : toto) {
//            if ((b.getName().equals(nameBeer)) && (b.getDegree() == degree)) {
//                beerService.delete(nameBeer);
//                response = new ResponseEntity<>(HttpStatus.OK);
//            } else {
//                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }
//        return response;
//    }
//
//    @RequestMapping(
//            value = "/getBars/{nameBeer}",
//            method = RequestMethod.POST)
//
//    @ResponseBody
//    public ResponseEntity<List<Bar>> getBarsWithThisBeer(
//            @PathVariable("nameBeer") String nameBeer) {
//        List<Bar> getBarsWithThisBeer = beerService.getBarsWithThisBeer(nameBeer);
//        if (beerService.getBarsWithThisBeer(nameBeer).isEmpty()) {
//            return new ResponseEntity<>(getBarsWithThisBeer, HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<>(getBarsWithThisBeer, HttpStatus.OK);
//        }
//    }
}