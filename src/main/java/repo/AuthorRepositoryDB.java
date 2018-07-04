package repo;

import model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by Raul on 08/05/2018.
 */

@Repository("database")
public class AuthorRepositoryDB implements IAuthorRepository {

    @Autowired
    private AuthorRepository authorRepository;

    //@PostConstruct
    public void init(){
        populateDB();
    }

    private void populateDB(){
        authorRepository.deleteAll();
        String fileName = "src/main/resources/users.txt";
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line ->{
                String[] splitResult = line.split(" ");
                //System.out.println(splitResult[0] + " " + splitResult[1] + " " + splitResult[2]);
                authorRepository.save(new Author(splitResult[0], splitResult[1], splitResult[2]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(authorRepository.count());
    }

    @Override
    public Collection<Author> searchAuthorsByName(String queryString) {
        //return authorRepository.findAuthorsByFirstnameContaining(queryString);
        return authorRepository.findAuthorsByFirstnameContainingOrSurnameContaining(queryString, queryString);
        //return null;
    }

    @Override
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }
}
