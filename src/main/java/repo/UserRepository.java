package repo;

import model.Report;
import model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Raul on 23/11/2017.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmailAndPasswordAndActive(String email, String pasword,Boolean active);
    Iterable<User> findByActive(Boolean active);

}