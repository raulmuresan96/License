package repo;

import model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by Raul on 08/05/2018.
 */

@Repository("database")
public class AuthorRepositoryDB implements IAuthorRepository {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Collection<Author> searchAuthorsByName(String queryString) {
        return authorRepository.findAuthorsByFirstnameContaining(queryString);
        //return null;
    }

    @Override
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }
}
