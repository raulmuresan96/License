package repo;

import model.Author;
import model.Trie;
import model.TrieNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Raul on 17/04/2018.
 */
@Service
public class AuthorTrieRepository {

    private final AuthorRepository authorRepository;

    private Trie trie;

    private Map<String, Author> authorsIds;
    private Map<Author, Boolean> authors;

    @Autowired
    public AuthorTrieRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @PostConstruct
    public void init(){
        authors = new ConcurrentHashMap<>();
        authorsIds = new ConcurrentHashMap<>();
        populateTrie();


        //System.out.println(authors);
    }

    public void populateTrie(){
        Iterable<Author> iterable = authorRepository.findAll();
        trie = new Trie();

        //try (PrintWriter writer = new PrintWriter("/Users/Raul/Documents/SpringBootExample/src/test/resources/authors.txt", "UTF-8");){


            iterable.forEach(author -> {
                authors.put(author, true);
                authorsIds.put(author.getAuthorId(), author);

                //writer.println("The first line");

                //writer.println(author.getAuthorId() + "#" + author.getFirstname() + "#" + author.getSurname());
                //System.out.println(author.getAuthorId() + " " + author.getFirstname() + " " + author.getSurname());

                String authorId = author.getAuthorId();
                splitStringIntoWords(authorId, author.getFirstname().toLowerCase());
                splitStringIntoWords(authorId, author.getSurname().toLowerCase());
            });

        /*} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


    }


    public Iterable<Author> findAll(){
        return authorRepository.findAll();
    }


    private void splitStringIntoWords(String authorId, String string){
        String[] words = string.split("[ ]");
        for(int i = 0; i < words.length; i++){
            //System.out.println(words[i]);
            if(!words[i].matches("[a-z]+"))
                continue;
            //System.out.println("Name " + words[i]);
            insertIntoTrie(authorId, words[i]);
        }

    }

    public void insertIntoTrie(String authorId, String string){
        for(int i = 0; i < string.length(); i++){
            //System.out.println(string.substring(i));
            trie.insert(string, i, authorId);
        }
    }


    public Collection<Author> convertIdsToAuthors(Collection<String> ids){
        List<Author> authors = new ArrayList<>();
        for (String id: ids){
            authors.add(authorsIds.get(id));
        }
        return authors;
    }

    public Author save(Author author){
        return authorRepository.save(author);
    }

    public Collection<Author> searchAuthors(String[] names){
        if(names.length == 0)
            return null;
        TrieNode trieNode = trie.getNodeForString(names[0]);
        if(trieNode == null){
            return new ArrayList<>();
        }
        Set<String> queryResult = new HashSet<>();
        queryResult.addAll(trieNode.authors.keySet());
        for(int i = 1; i < names.length; i++){
            //System.out.println("ajunge " + names[i]);
            trieNode = trie.getNodeForString(names[i]);
            Set<String> currentNameResult = trieNode.authors.keySet();
            //System.out.println(currentNameResult);
            queryResult.retainAll(currentNameResult);
        }
        return convertIdsToAuthors(queryResult);
//        );
//        for (String string :trieNode.authors.keySet()){
//            System.out.println(authorsIds.get(string).getFirstname() + " " + authorsIds.get(string).getSurname());
//        }
//        //trie.
//        return null;
    }
}