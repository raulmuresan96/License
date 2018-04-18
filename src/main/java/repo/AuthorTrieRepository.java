package repo;

import model.Author;
import model.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Raul on 17/04/2018.
 */
@Service
public class AuthorTrieRepository {

    @Autowired
    private AuthorRepository authorRepository;

    private Trie trie;

    private Map<String, Author> authorsIds;
    private Map<Author, Boolean> authors;


    @PostConstruct
    public void init(){
        authors = new ConcurrentHashMap<>();
        authorsIds = new ConcurrentHashMap<>();
        Iterable<Author> iterable = authorRepository.findAll();
        trie = new Trie();

        iterable.forEach(author -> {
            authors.put(author, true);
            authorsIds.put(author.getAuthorId(), author);

            System.out.println(author.getAuthorId() + " " + author.getFirstname() + " " + author.getSurname());
            String authorId = author.getAuthorId();
            splitStringIntoWords(authorId, author.getFirstname().toLowerCase());
            splitStringIntoWords(authorId, author.getSurname().toLowerCase());

        });


        System.out.println(authors);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // start your monitoring in here
    }

    private void splitStringIntoWords(String authorId, String string){
        String[] words = string.split("[ -]");
        for(int i = 0; i < words.length; i++){
            System.out.println(words[i]);
            if(!words[i].matches("[a-z]+"))
                continue;
            System.out.println("Name " + words[i]);
            insertIntoTrie(authorId, words[i]);
        }

    }

    public void insertIntoTrie(String authorId, String string){
        for(int i = 0; i < string.length(); i++){
            System.out.println(string.substring(i));
            trie.insert(string, i, authorId);
        }

    }





    public Author save(Author author){
        return authorRepository.save(author);
    }



    public Collection<Author> searchAuthors(String queryString){
        //trie.
        return null;
    }
}
