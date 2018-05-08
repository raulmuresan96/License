package repo;

import model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Raul on 08/05/2018.
 */

@Repository("parallel")
public class AuthorRepositoryParallel implements IAuthorRepository {

    @Autowired
    private AuthorRepository authorRepository;
    private  Map<Author, Boolean> authors;

    @PostConstruct
    public void init(){
        authors = new ConcurrentHashMap<>();
        authorRepository.searchAuthors().forEach(author ->{
            authors.put(author, true);
        });
        System.out.println(authors);
    }

    private Set<Author> searchAuthorsByWord(String queryString) {
        Set<Author> searchResult = new HashSet<>();
        for(Author author: authors.keySet()){
            if(author.getSurname().contains(queryString) || author.getFirstname().contains(queryString) ){
                searchResult.add(author);
            }
        }
        return searchResult;
    }


    @Override
    public Collection<Author> searchAuthorsByName(String queryString) {
        String[] names = queryString.split("[ -]");
        if(names.length == 0)
            return null;

        //return authorRepository.findAuthorsByFirstnameContaining(names[0]);

        Set<Author> queryResult = new HashSet<>();
        queryResult.addAll(searchAuthorsByWord(names[0]));
        for(int i = 1; i < names.length; i++){

            Set<Author> currentNameResult = searchAuthorsByWord(names[i]);
            //System.out.println(currentNameResult);
            queryResult.retainAll(currentNameResult);
        }
        return queryResult;

    }

    @Override
    public Author addAuthor(Author author) {
        Author result = authorRepository.save(author);
        authors.put(author, true);
        return result;
    }

}
