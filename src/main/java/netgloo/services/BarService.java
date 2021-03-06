package netgloo.services;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.models.IBarDao;
import netgloo.models.IBeerDao;
import org.json.JSONArray;
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
    private IBarDao IBarDao;

    @Autowired
    IBeerDao IBeerDao;

    /**
     * Get all the bars in the database
     * @return listbars
     */
    public Iterable<Bar> all()
    {
        return IBarDao.findAll();
    }

    /**
     * Get bar from it's id
     * @param id : id for the bar
     * @return bar found
     */
    public Bar getById(long id)
    {
        return IBarDao.findOne(id);
    }

    /**
     * Update bar
     * @param bar : bar to update
     * @return bar updated
     */
    public Bar Update(Bar bar)
    {
        if(!Exist(bar.getBarId()))
            return null;
        return IBarDao.save(bar);
    }

    /**
     * find a bar with it's name
     * @param name : name of the bar to search
     * @return bar found
     */
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

    /**
     * add a beer into a bar
     * @param nameBar : bar to update beer
     * @param beer : beer to add into the bar
     * @param beerService
     * @return Bar updated with his new beer
     */
    public Bar addBeerToBar(String nameBar,
                            Beer beer, BeerService beerService)
    {
        // Get beer from his name from DataBase
        Beer beerExist = beerService.getByName(beer.getName());

        boolean isInList = false;

        // Check if the beer exist
        if(beerExist == null)
        {
            // create beer
            beerService.create(beer);
        }

        // Get Bar from his name
        Bar bar = this.getByName(nameBar);

        if(bar == null)
            return null;
        else
        {
            // Get listbeer from this bar
            Set<Beer> listBeer = bar.getListBeer();
            for (Beer oneBeer : listBeer)
            {
                if(oneBeer.getName().equals(beer.getName())) {
                    isInList = true;
                    break;
                }
            }
            if(!isInList)
            {
                // beer not on the listbeer : add it !
                bar.AddBeer(beer);
                // Update the bar with it's new listbeer
                Update(bar);
            }
        }
        return bar;
    }

    /**
     * Delete a bar
     * @param bar : bar to erase
     * @return bar deleted or not
     */
    public boolean delete(Bar bar)
    {
        boolean ret = false;
        if(delete(bar.getBarId())) {
            ret = true;
        }
        return ret;
    }

    /**
     * Search is a bar with it's id existed
     * @param id : id of the bar
     * @return bar exist or not
     */
    public boolean Exist(long id)
    {
        return getById(id) != null;
    }

    /**
     * Search if a bar exist from it's name
     * @param name : name of the bar
     * @return bar exist or not
     */
    public boolean nameExist(String name)
    {
        return getByName(name) != null;
    }

    /**
     * Delete a bar with it's id
     * @param id: id of the bar
     * @return bar deleted or not
     */
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

    /**
     * Delete a beer into a bar
     * @param beerToRemove : beer to remove
     * @param bar : bar in which to remove beer
     * @return
     */
    public boolean deleteBeerInBar(Beer beerToRemove, Bar bar)
    {
        boolean bRet = false;

        Set<Beer> listBeers = bar.getListBeer();
        for (Beer beer : listBeers)
        {
            if(beer.getName().equals(beerToRemove.getName()))
            {
                listBeers.remove(beer);
                bar.setListBeer(listBeers);
                bRet = true;
            }
        }

        return bRet;
    }

    /**
     * Delete a bar from it's name
     * @param name : name of the bar
     * @return
     */
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

    /**
     * Delete all the bars
     * @return
     */
    public String DeleteAll()
    {
        for(Bar bar : IBarDao.findAll())
        {
            IBarDao.delete(bar);
        }
        return "Delete all ok";
    }

    /**
     * Create a bar
     * @param bar : barto create
     * @return bar created
     */
    public Bar create(Bar bar)
    {
        boolean res = nameExist(bar.getName());
        if(res)
        {
            return null;
        }

        Bar barNew = getBarFromGooglePlaces(bar.getName(), bar.getCity());
        if(barNew != null)
            return IBarDao.save(barNew);
        else
            return IBarDao.save(bar);
    }

    /**
     * Create a bar with infos from API places
     *
     * @param nameBar : name of the bar
     * @param CityBar : City for the bar
     * @return
     */
    private Bar getBarFromGooglePlaces(String nameBar, String CityBar)
    {
        String search = nameBar,
                city = CityBar;

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

    /**
     * Get details for a bar with api places
     * @param respdet : jsonobject with reference of the bar
     * @return bar with it's field filled with details
     */
    private Bar getBarFromJsonDetails(JSONObject respdet)
    {
        try {
            Bar bar = new Bar();
            JSONArray address_components = respdet.getJSONArray("address_components");
            if (address_components == null) {
                System.out.println("error parsing array");
                return null;
            }

            if (address_components.length() == 0) {
                System.out.println("no result");
                return null;
            }
            JSONObject first = (JSONObject) (address_components.get(0));
            String address = first.getString("long_name");

            first = (JSONObject) (address_components.get(1));
            address += ", " + first.getString("long_name");
            bar.setAddress(address);

            first = (JSONObject) (address_components.get(2));
            bar.setCity(first.getString("long_name"));

            first = (JSONObject) (address_components.get(4));
            bar.setPostalCode(first.getString("long_name"));

            bar.setDescription("");
            bar.setName(respdet.getString("name"));

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

    /**
     * Set Longitude/Latitude for the bar
     * @param bar : bar to be updated
     * @param respGeometry data with geolocation long/lat
     */
    private void setBarCoordinates(Bar bar, JSONObject respGeometry)
    {
        JSONObject location = respGeometry.getJSONObject("location");

        bar.setLatitude(location.getDouble("lat"));
        bar.setLongitude(location.getDouble("lng"));
    }
}
