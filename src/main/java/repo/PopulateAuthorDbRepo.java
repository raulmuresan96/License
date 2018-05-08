package repo;

import model.Author;
import model.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by Raul on 07/05/2018.
 */
@Repository
public class PopulateAuthorDbRepo {
    @Autowired
    //private IAuthorRepository authorRepository;
    private AuthorRepository authorRepository;



//    @Autowired
//    public PopulateAuthorDbRepo(AuthorRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }

    @PostConstruct
    public void init(){
        //authorRepository.sa
        System.out.println("AJUNGE LA INIT " + authorRepository);
    }

    public void addAll(List<Author> authors){
        authorRepository.deleteAll();
        authorRepository.save(authors);

    }
}
