package netgloo.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface IBeerDao extends CrudRepository<Beer, Long> {

} // class IBeerDao
