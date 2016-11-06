package netgloo.services;

import netgloo.models.IUserDao;
import netgloo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by vro on 04/11/16.
 */
@Service
public class LoginService {

    @Autowired
    IUserDao iUserDao;

    public Iterable<User> all()
    {
        Iterable<User> listUsers = iUserDao.findAll();

        return listUsers;
    }

    public User getByName(String name)
    {
        Iterable<User> list = iUserDao.findAll();
        for(User b : list)
        {
            if(b.getUsername().equals(name)) {
                return b;
            }
        }
        return null;
    }

    public User create(User user)
    {
        User newUser = iUserDao.save(user);
        return newUser;
    }

    public boolean comparePassword(User user, String password)
    {
        if(user.getPassword().equals(password))
            return true;
        return false;
    }

    public User Update(User user)
    {
        if(!Exist(user.getUserId()))
            return null;
        return iUserDao.save(user);
    }

    private boolean Exist(long userId)
    {
        return getById(userId) != null;
    }

    public User getById(long id)
    {
        return iUserDao.findOne(id);
    }

}
