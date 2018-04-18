package repo;

import model.Journal;
import model.Location;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Iulian on 11/25/2017.
 */
public interface JournalRepository extends CrudRepository<Journal, String> {

}
