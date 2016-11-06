package netgloo.services;

import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.models.IBarDao;
import netgloo.models.IBeerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vro on 13/10/16.
 */
@Service
public class BeerService {

    @Autowired
    IBeerDao IBeerDao;

    @Autowired
    IBarDao IBarDao;

    /**
     * Get all the beers
     * @return
     */
    public Iterable<Beer> all()
    {
        return IBeerDao.findAll();
    }

    /**
     * Get beer with it's id
     * @param id
     * @return
     */
    public Beer get(long id)
    {
        return IBeerDao.findOne(id);
    }

    /**
     * Get barwith it's name
     * @param name : name of the bar
     * @param degree : degree of the beer
     * @return
     */
    public Beer get(String name, long degree)
    {
        Beer beer = null;
        for(Beer allBeer : IBeerDao.findAll())
        {
            if(allBeer.getName().equals(name) && allBeer.getDegree() == degree) {
                beer = allBeer;
                break;
            }
        }
        return beer;
    }

    /**
     * Delete all the beers in database
     * @return
     */
    public String deleteAllBeer()
    {
//        for(Beer allBeer : IBeerDao.findAll())
//        {
//        }
        IBeerDao.delete(IBeerDao.findAll());
        return "Delete All Beers ok";
    }

    /**
     * Get beer with it's name
     * @param nameBeer : beer to search
     * @return
     */
    public Beer getByName(String nameBeer)
    {
        Beer beer = new Beer();
        Iterable<Beer> it = IBeerDao.findAll();

        if(it == null)
            beer = null;
        for(Beer allBeer : it)
        {
            if(allBeer.getName().equals(nameBeer))
                beer = allBeer;
            else
                beer = null;
        }
        return beer;
    }

    /**
     * Update beer
     * @param beer : beer to update
     * @return
     */
    public Beer Update(Beer beer)
    {
        for(Beer beerToUpdate : IBeerDao.findAll())
        {
            if(beerToUpdate.getName().equals(beer.getName()))
            {
//                beerToUpdate.setName(beer.getName());
                beerToUpdate.setDegree(beer.getDegree());
                IBeerDao.save(beerToUpdate);
            }
        }
        return beer;
    }

    /**
     * Create a beer into the database
     * @param beer : beer to create
     * @return
     */
    public boolean create(Beer beer)
    {
        if(beer.getBeerId() == 0)
        {
            IBeerDao.save(beer);
            return true;
        }
        return false;
    }

    /**
     * Delete a beer into the database
     * @param beer to create
     * @return
     */
    public boolean delete(Beer beer)
    {
        boolean ret = false;
        for(Bar bars : IBarDao.findAll())
        {
            for(Beer beers : IBeerDao.findAll())
            {
                if(beers == beer)
                {
                    bars.getListBeer().remove(beers);
                    IBarDao.save(bars);
                    IBeerDao.delete(beers);
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * Delete a beer into the database
     * @param beerToDelete : beer to delete
     * @return
     */
    public boolean delete(String beerToDelete)
    {
        boolean ret = false;
        for(Bar bars : IBarDao.findAll())
        {
            for(Beer beers : all())
            {
                if(beers.getName().equals(beerToDelete))
                {
                    bars.getListBeer().remove(beers);
                    IBarDao.save(bars);
                    IBeerDao.delete(beers);
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * Get all the bars containing this beer
     * @param nameBeer
     * @return
     */
    public List<Bar> getBarsWithThisBeer(String nameBeer)
    {
        List<Bar> listBar = new ArrayList<>();

        for(Bar bars : IBarDao.findAll())
        {
            for(Beer beers : bars.getListBeer())
            {
                if(beers.getName().equals(nameBeer))
                {
                    listBar.add(bars);
                }
            }
        }

        return listBar;
    }
}
