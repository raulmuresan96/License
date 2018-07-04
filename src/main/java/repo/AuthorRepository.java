package repo;

import model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Raul on 17/04/2018.
 */
public interface AuthorRepository extends CrudRepository<Author, String> {

    @Query("SELECT new Author(author.authorId, author.firstname, author.surname) FROM Author author")
    List<Author> searchAuthors();

    List<Author> findAuthorsByFirstnameContainingOrSurnameContaining(String firstname, String surname);

    //List<Author> findAuthorsByFirstnameContainingOrSurnameContaining(String firstname);

    List<Author> findAuthorsBySurnameContaining(String username);


//    @Query("SELECT new Author(author.authorId, author.firstname, author.surname) FROM Author author where author.firstname like: %query% ")
//    List<Author> querySearch(@Param("parent") String query);
}
