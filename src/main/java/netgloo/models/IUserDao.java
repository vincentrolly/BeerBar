package netgloo.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface IUserDao extends CrudRepository<User, Long> {

} // class IUserDao
