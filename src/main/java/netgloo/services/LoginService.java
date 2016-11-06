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

    /**
     * Get all the users
     * @return
     */
    public Iterable<User> all()
    {
        Iterable<User> listUsers = iUserDao.findAll();

        return listUsers;
    }

    /**
     * Get user with it's name
     * @param name : user to find
     * @return
     */
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

    /**
     * Create a user into the database
     * @param user : user to create
     * @return
     */
    public User create(User user)
    {
        User newUser = iUserDao.save(user);
        return newUser;
    }

    /**
     * Compare password given with the one into the database
     * @param user : user
     * @param password
     * @return
     */
    public boolean comparePassword(User user, String password)
    {
        if(user.getPassword().equals(password))
            return true;
        return false;
    }

    /**
     * update the user into the database
     * @param user : user to update
     * @return
     */
    public User Update(User user)
    {
        if(!Exist(user.getUserId()))
            return null;
        return iUserDao.save(user);
    }

    /**
     * Check if exist the user
     * @param userId : id of the user
     * @return
     */
    private boolean Exist(long userId)
    {
        return getById(userId) != null;
    }

    /**
     * Get the user with it's id
     * @param id : id of the user
     * @return
     */
    public User getById(long id)
    {
        return iUserDao.findOne(id);
    }
}
