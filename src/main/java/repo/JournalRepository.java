package repo;

import model.Journal;
import org.springframework.data.repository.CrudRepository;


public interface JournalRepository extends CrudRepository<Journal, String> {

}
