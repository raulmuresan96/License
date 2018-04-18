package repo;

import model.Author;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Raul on 17/04/2018.
 */
public interface AuthorRepository extends CrudRepository<Author, String> {
}
