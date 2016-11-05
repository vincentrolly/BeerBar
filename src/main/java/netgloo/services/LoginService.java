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
    private static IUserDao iUserDao;

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

    public static User getById(long id)
    {
        return iUserDao.findOne(id);
    }

}
