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

/**
 * Created by vro on 13/10/16.
 */
@Service
public class BarService {

    @Autowired
    IBarDao IBarDao;

    public Iterable<Bar> all()
    {
        return IBarDao.findAll();
    }

    public Bar get(long id)
    {
        return IBarDao.findOne(id);
    }

    public Bar get(String name)
    {
        Bar bar = null;
        for(Bar b : IBarDao.findAll())
        {
            if(b.getName().equals(name)) {
                bar = b;
                break;
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

//    public List<Bar> getByName(String name)
//    {
//        List<Bar> bars = new ArrayList<>();
//        for(Bar b : IBarDao.findAll())
//        {
//            if(b.getName().equals(name))
//                bars.add(b);
//        }
//        return bars;
//    }

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

    public boolean create(Bar bar)
    {
        if(bar.getBarId() < 1)
        {
            IBarDao.save(bar);
            return true;
        }
        return false;
    }

    public boolean update(Bar bar)
    {
        if(bar.getBarId() > 0)
        {
            IBarDao.save(bar);
            return true;
        }
        return false;
    }

    public boolean delete(Bar bar)
    {
        boolean ret = false;
        if(delete(bar.getBarId())) {
            ret = true;
        }
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

    public boolean delete(String name)
    {
        boolean ret = false;

        for(Bar bar : IBarDao.findAll())
        {
            if(bar.getName().equals(name))
            {
                bar.getListBeer().clear();

                IBarDao.delete(bar);
                ret = true;
            }
        }
        return ret;
    }

}
