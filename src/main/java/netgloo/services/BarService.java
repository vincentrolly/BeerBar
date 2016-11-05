package netgloo.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import netgloo.controllers.BeerCtrl;
import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.models.IBarDao;
import netgloo.models.IBeerDao;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import netgloo.services.GooglePlacesService;

/**
 * Created by vro on 13/10/16.
 */
@Service
public class BarService {

    @Autowired
    IBarDao IBarDao;

//    private void addExampleBeer(Bar bar)
//    {
//        Beer beer = new Beer();
//        beer.setDegree(4);
//        beer.setBeerId(20);
//        beer.setName("Dechire sa race");
//        bar.addBeer(beer, 33, 15);
//
//        //return bar;
//    }

    public Iterable<Bar> all()
    {
        Iterable<Bar> listBars = IBarDao.findAll();

        return listBars;
    }

    public Bar getById(long id)
    {
        return IBarDao.findOne(id);
    }

    public Bar Update(Bar bar)
    {
        if(!Exist(bar.getBarId()))
            return null;
        return IBarDao.save(bar);
    }

//    public List<Bar> getAllByName(String name)
//    {
//        List<Bar> bars = new ArrayList<>();
//        for(Bar b : IBarDao.findAll())
//        {
//            if(b.getName().equals(name))
//                bars.add(b);
//        }
//        return bars;
//    }

    public List<Bar> getAll()
    {
        List<Bar> bars = new ArrayList<>();
        for(Bar b : IBarDao.findAll())
        {
            bars.add(b);
        }
        return bars;
    }

    public Bar getByName(String name)
    {
        for(Bar b : IBarDao.findAll())
        {
            if(b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

//    public Bar get(long id)
//    {
//        return IBarDao.findOne(id);
//    }

    public boolean delete(Bar bar)
    {
        boolean ret = false;
        if(delete(bar.getBarId())) {
            ret = true;
        }
        return ret;
    }

    public boolean Exist(long id)
    {
        return getById(id) != null;
    }

    public boolean nameExist(String name)
    {
        return getByName(name) != null;
    }

    public boolean delete(long id)
    {
        boolean ret = false;
        if(IBarDao.findOne(id) != null)
        {
            IBarDao.delete(id);
            ret = true;
        }
        return ret;
    }

    public boolean deleteBeerInBar(Beer beerToRemove, Bar bar)
    {
        boolean bRet = false;

//        Set<Beer> listBeers = bar.getListBeer();
//        for (Beer beer : listBeers)
//        {
//            if(beer.getName().equals(beerToRemove.getName()))
//            {
//                listBeers.remove(beer);
//                bar.setListBeer(listBeers);
//            }
//        }

        return bRet;
    }

    public boolean delete(String name)
    {
        boolean ret = false;
//
//        for(Bar bar : IBarDao.findAll())
//        {
//            if(bar.getName().equals(name))
//            {
//                bar.getListBeer().clear();
//
//                IBarDao.delete(bar);
//                ret = true;
//            }
//        }
        return ret;
    }

    public String DeleteAll()
    {
        for(Bar bar : IBarDao.findAll())
        {
            IBarDao.delete(bar);
        }
        return "Delete all ok";
    }

    public Bar create(String nameBar)
    {
        boolean res = nameExist(nameBar);
        if(res)
        {
            return null;
        }

                //  TODO : call to api places google
        Bar bar = getBarFromGooglePlaces(nameBar);


//        Bar bar = new Bar();
//        bar.setName(nameBar);
//
//        bar.setAddress("t4t1g4t1g");
//        bar.setCity("Lyon");
//        bar.setDescription("titi toto tata");
//        bar.setLatitude(4.7);
//        bar.setListBeer(new HashSet<Beer>());
//        bar.setLongitude(5.8);
//        bar.setPostalCode("13579");

        return IBarDao.save(bar);
    }

    /**
     * Creer un bar a partir des infos provenant de l'API places
     *
     * @param nameBar : nom du bar a chercher
     * @return
     */
    private Bar getBarFromGooglePlaces(String nameBar)
    {
        String search = nameBar,
                city = "FRANCE";

        // appel de l'api places et recuperation de la reponse json
        JSONObject resp = GooglePlacesService.TextSearch(search, city);
        if(resp == null)
        {
            System.out.println("error parsing json");
            return null;
        }

        // recuperer la reference a partir du json
        String reference = GooglePlacesService.getBarReferenceFromJsonText(resp);
        if(reference == null)
        {
            System.out.println("error reference");
            return null;
        }

        // interroger api_places details avec la reference et recuperation objet JSON
        JSONObject respdet = GooglePlacesService.Details(reference);
        if(respdet == null)
        {
            System.out.println("error details");
            return null;
        }

        // on cree un bar a partir des infos de l'api places
        Bar bar = getBarFromJsonDetails(respdet.getJSONObject("result"));
        if(bar == null)
        {
            System.out.println("error creation bar");
            return null;
        }
//        bar.setName(nameBar);
        // On renvoie l'objet bar trouve
        return bar;
    }

    private Bar getBarFromJsonDetails(JSONObject respdet)
    {
        try {
            Bar bar = new Bar();
            String formatted_address = respdet.getString("formatted_address");
            String[] splitted_address = formatted_address.split(", ");

            bar.setAddress(splitted_address[0]);
            bar.setPostalCode(splitted_address[1]);
            bar.setCity(splitted_address[2]);
            bar.setDescription("");
            bar.setName(respdet.getString("name"));

            // TODO : check if reference ok
            setBarCoordinates(bar, respdet.getJSONObject("geometry"));

            return bar;
        }
        catch(Exception e)
        {
            System.out.println("\t" + "getBarFromJsonDetails error json");
            System.out.println("\t" + e.getMessage());
            return null;
        }
    }

    private void setBarCoordinates(Bar bar, JSONObject respGeometry)
    {
        JSONObject location = respGeometry.getJSONObject("location");

        bar.setLatitude(location.getDouble("lat"));
        bar.setLongitude(location.getDouble("lng"));
    }
}
