package repo;

import model.Author;

import java.util.Collection;

/**
 * Created by Raul on 07/05/2018.
 */
public interface IAuthorRepository {
    Collection<Author> searchAuthorsByName(String queryString);

    Author addAuthor(Author author);

//    void deleteAll();
//
//    Iterable<Author> save(Iterable<Author> authors);
}
