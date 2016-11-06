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

    public Iterable<Beer> all()
    {
        return IBeerDao.findAll();
    }

    public Beer get(long id)
    {
        return IBeerDao.findOne(id);
    }

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

    public String deleteAllBeer()
    {
//        for(Beer allBeer : IBeerDao.findAll())
//        {
//        }
        IBeerDao.delete(IBeerDao.findAll());
        return "Delete All Beers ok";
    }

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

    public List<Beer> getAll()
    {
        List<Beer> listbeers = new ArrayList<>();
        for(Beer beer : IBeerDao.findAll())
        {
            listbeers.add(beer);
        }
        return listbeers ;
    }

    public List<Beer> getAllByName(String name)
    {
        List<Beer> beers = new ArrayList<>();
        for(Beer beer: IBeerDao.findAll())
        {
            if(beer.getName().equals(name))
                beers.add(beer);
        }
        return beers;
    }

    public boolean create(Beer beer)
    {
        if(beer.getBeerId() == 0)
        {
            IBeerDao.save(beer);
            return true;
        }
        return false;
    }



    public boolean delete(Beer beer)
    {
        boolean ret = false;
//        for(Bar bars : IBarDao.findAll())
//        {
//            for(Beer beers : IBeerDao.findAll())
//            {
//                if(beers == beer)
//                {
//                    bars.getListBeer().remove(beers);
//                    IBarDao.save(bars);
//                    IBeerDao.delete(beers);
//                    ret = true;
//                }
//            }
//        }
        return ret;
    }

    public boolean delete(String beerToDelete)
    {
        boolean ret = false;
//        for(Bar bars : IBarDao.findAll())
//        {
//            for(Beer beers : IBeerDao.findAll())
//            {
//                if(beers.getName().equals(beerToDelete))
//                {
//                    bars.getListBeer().remove(beers);
//                    IBarDao.save(bars);
//                    IBeerDao.delete(beers);
//                    ret = true;
//                }
//            }
//        }
        return ret;
    }

    public List<Bar> getBarsWithThisBeer(String nameBeer)
    {
        List<Bar> listBar = new ArrayList<>();
//
//        for(Bar bars : IBarDao.findAll())
//        {
//            for(Beer beers : bars.getListBeer())
//            {
//                if(beers.getName().equals(nameBeer))
//                {
//                    listBar.add(bars);
//                }
//            }
//        }

        return listBar;
    }
}

