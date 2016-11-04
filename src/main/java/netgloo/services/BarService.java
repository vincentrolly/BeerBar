package netgloo.services;

import netgloo.controllers.BeerCtrl;
import netgloo.models.Bar;
import netgloo.models.Beer;
import netgloo.models.IBarDao;
import netgloo.models.IBeerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public Bar Update(Bar bar)
    {
        for(Bar barToUpdate : IBarDao.findAll())
        {
            if(barToUpdate.getBarId() == bar.getBarId()) {

                barToUpdate.setAddress(bar.getAddress());
                barToUpdate.setCity(bar.getCity());
                barToUpdate.setDescription(bar.getDescription());
                barToUpdate.setLatitude(bar.getLatitude());
                barToUpdate.setLongitude(bar.getLongitude());
                barToUpdate.setName(bar.getName());
                barToUpdate.setPostalCode(bar.getPostalCode());
                bar = IBarDao.save(barToUpdate);
            }
        }
        return bar;
    }

    public List<Bar> getAllByName(String name)
    {
        List<Bar> bars = new ArrayList<>();
        for(Bar b : IBarDao.findAll())
        {
            if(b.getName().equals(name))
                bars.add(b);
        }
        return bars;
    }

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
        Bar bar = new Bar();
        for(Bar b : IBarDao.findAll())
        {
            if(b.getName().equals(name))
                bar = b;
        }
        return bar;
    }

    public Bar get(long id)
    {
        return IBarDao.findOne(id);
    }

    public boolean delete(Bar bar)
    {
        boolean ret = false;
        if(delete(bar.getBarId())) {
            ret = true;
        }
        return ret;
    }

    public Bar create(String namebar)
    {
        Bar bar;
        if(isExist(namebar))
        {
            bar = getByName(namebar);
        }
        else
        {
            bar = new Bar();
            bar.setName(namebar);
            bar.setAddress("t4t1g4t1g");
            bar.setCity("Lyon");
            bar.setDescription("titi toto tata");
            bar.setLatitude(4.7);
            bar.setListBeer(null);
            bar.setLongitude(5.8);
            bar.setPostalCode("13579");
            IBarDao.save(bar);
        }
        return bar;
    }

    public boolean isExist(String nameBar)
    {
        boolean ret = false;
        if(getByName(nameBar) != null)
            ret = true;
        return ret;
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
}
