package searchTests;

import model.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import repo.AuthorRepository;
import repo.AuthorTrieRepository;
import service.AuthorService;
import start.Application;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * Created by Raul on 19/04/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = Application.class)
public class SearchTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorTrieRepository authorRepository;


    private Set<Author> authors;

    @PostConstruct
    public void init(){
        try (Stream<String> stream = Files.lines(Paths.get("/Users/Raul/Documents/SpringBootExample/src/test/resources/authors.txt"))) {

            stream.forEach(currentLine ->{
                String[] words = currentLine.split("#");
                String authorId = words[0];
                String firstname = words[1];
                String surname = words[2];
                authorRepository.save(new Author(authorId, firstname, surname));
                //System.out.println(authorId + " " + firstname + " " + surname);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        authorRepository.populateTrie();
            //authorRepository.save(new Author("123","123","123"));
            //Thread.sleep(1000);
        System.out.println("inceputurile!!!!!!");
    }

    public Set<Author> searchAuthors(String queryString){
        //System.out.println(queryString);
        Set<Author> searchResults = new HashSet<>();
        for(Author author: authors){
            //String fullName =  + " " + author.getSurname().toLowerCase();
            String firstname = author.getFirstname().toLowerCase();
            String surname = author.getSurname().toLowerCase();
//            if((firstname.matches("[a-z ]*") && firstname.contains(queryString)) ||
//                    (surname.matches("[a-z ]*") && surname.contains(queryString)) ){
//                searchResults.add(author);
//            }

            if(splitStringIntoWords(firstname, queryString) || splitStringIntoWords(surname, queryString)){
                searchResults.add(author);
            }
//            if(fullName.contains(queryString) && ){
//                searchResults.add(author);
//            }
        }
        return searchResults;
    }


    public boolean identicalCollections(Set<Author> collection1, Collection<Author> collection2){
        System.out.println(collection1.size() + " " + collection2.size());
        if(collection1.size() != collection2.size())
            return false;
        for(Author author: collection2){
            if(!collection1.contains(author))
                return false;
        }
        return true;
    }

    private boolean splitStringIntoWords(String name, String queryString){
        String[] words = name.split("[ ]");
        for(int i = 0; i < words.length; i++){
            //System.out.println(words[i]);
            if(!words[i].matches("[a-z]+"))
                continue;
            if(words[i].contains(queryString)){
                return true;
            }
            //insertIntoTrie(authorId, words[i]);
        }
        return false;
    }



    @Test
    public void testExample() throws Exception {
        Random random = new Random();
        System.out.println("Dupa inceputuri!!!!!!");
        //System.out.println(authorRepository.findAll());

        authors = new HashSet<>();

        authorRepository.findAll().forEach(a ->{
            //if((a.getFirstname() + a.getSurname()).matches("[a-zA-Z \\\\-]+"))
            authors.add(a);});
        System.out.println(authors);

        List<String> aux= new ArrayList<>();
        authorRepository.findAll().forEach(author -> {
            //if(author.getSurname().matches())
                    aux.addAll(Arrays.asList(author.getSurname().split(" ")));
                    aux.addAll(Arrays.asList(author.getFirstname().split(" ")));
                    //authors::add
        }
        );

        List<String> authorsName = aux.stream().filter(a -> a.matches("[a-zA-Z ]+")).collect(Collectors.toList());

        System.out.println(authorsName);


        for(int i = 0 ; i < 100_000; i++){
            String queryString = RandomGenerator.generateQueryStringFromAuthors(authorsName);
            System.out.println(queryString);
            //searchAuthors(queryString);
            System.out.println(searchAuthors(queryString));
            String[] names = new String[1];
            names[0] = queryString;
            System.out.println(authorRepository.searchAuthors(names));

            assertTrue(identicalCollections(searchAuthors(queryString), authorRepository.searchAuthors(names)));
            //System.out.println();
            //System.out.println(RandomGenerator.generateRandom(1, 8));
        }
//        this.entityManager.persist(new User("sboot", "1234"));
//        User user = this.repository.findByUsername("sboot");
//        assertThat(user.getUsername()).isEqualTo("sboot");
//        assertThat(user.getVin()).isEqualTo("1234");
    }

}
