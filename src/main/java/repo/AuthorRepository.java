package repo;

import model.Author;
import model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Raul on 17/04/2018.
 */
public interface AuthorRepository extends CrudRepository<Author, String> {

    @Query("SELECT new Author(author.authorId, author.firstname, author.surname) FROM Author author")
    List<Author> searchAuthors();

    List<Author> findAuthorsByFirstnameContaining(String username);


//    @Query("SELECT new Author(author.authorId, author.firstname, author.surname) FROM Author author where author.firstname like: %query% ")
//    List<Author> querySearch(@Param("parent") String query);
}
