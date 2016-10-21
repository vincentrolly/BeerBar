package netgloo.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface IBarDao extends CrudRepository<Bar, Long> {

} // class IBarrDao
