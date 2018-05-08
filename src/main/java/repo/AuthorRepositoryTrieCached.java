package repo;

import model.Author;
import model.Trie;
import model.TrieNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Created by Raul on 08/05/2018.
 */
@Repository("trieCached")
public class AuthorRepositoryTrieCached implements IAuthorRepository {

    @Autowired
    private AuthorRepository authorRepository;
    private final ConcurrentMap<String, Future<TrieNode>> cache = new ConcurrentHashMap<>();
    private Trie trie;
    private Map<String, Author> authorsIds;
    private Map<Author, Boolean> authors;
    private AtomicInteger nextAvailableId;

    @PostConstruct
    public void init(){
        authors = new ConcurrentHashMap<>();
        authorsIds = new ConcurrentHashMap<>();
        trie = new Trie();
        nextAvailableId = new AtomicInteger(1);
        populateTrie();
    }

    public void populateTrie(){
        //System.out.println(authorRepository.searchAuthors());
        Iterable<Author> iterable = authorRepository.searchAuthors();
        //4Iterable<Author> iterable = authorRepository.findAll();
        System.out.println(iterable);
        trie = new Trie();



        iterable.forEach(author -> {
            authors.put(author, true);
            authorsIds.put(author.getAuthorId(), author);

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


    private void splitStringIntoWords(String authorId, String string){
        if(string == null)
            return;
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


//    private void loadUsersFromFile(){
//        try (Stream<String> stream = Files.lines(Paths.get("/Users/Raul/Desktop/ProgramareParalelaSem6/src/com/company/authors.txt"))) {
//            //stream.forEach(word -> trie.insert(word, 0));
//            stream.forEach(user -> {
//                int wordId = nextAvailableId.getAndIncrement();
//                //System.out.println(wordId);
//                authors.put(user, true);
//                authorsIds.put(wordId, user);
//
//                for(int i = 0; i < user.length(); i++){
//                    trie.insert(user, i, wordId);
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Loading from File Finished");
//    }

//    private Set<String> convertIdsToStrings(Collection<Integer> ids){
//        Set<String> searchResult = new HashSet<>();
//        for(int nr: ids){
//            searchResult.add(authorsIds.get(nr));
//        }
//        return searchResult;
//
//    }

    @Override
    public Author addAuthor(Author author){
        if(authors.containsKey(author))
            return null;
        Author result = authorRepository.save(author);
        String currentId = author.getAuthorId();
        authors.put(author, true);
        authorsIds.put(currentId, author);
        insertIntoTrie(author.getAuthorId(), author.getFirstname().toLowerCase());
        insertIntoTrie(author.getAuthorId(), author.getSurname().toLowerCase());
        return result;
    }


//    @Override
//    public Author addAuthor(Author author) {
//        if(authors.containsKey(author))
//            return null;
//        //System.out.println("Add " + username);
//        String currentId = author.getAuthorId();
//        authors.put(author, true);
//        authorsIds.put(currentId, author);
//
//
//    }

//
//    @Override
//    public Collection<String> search(String searchString) throws InterruptedException {
//        return convertIdsToStrings(trie.search(searchString));
//    }

    private TrieNode computeSearch(String searchString){
        return trie.getNodeForString(searchString);
    }


    public Set<String> search(String searchString)  {
        //System.out.println(searchString);
//        return computeSearch(searchString);
        while (true) {
            Future<TrieNode> f = cache.get(searchString);
            if (f == null) {
                //System.out.println("Not Using cache");
                Callable<TrieNode> eval = () -> computeSearch(searchString);

                FutureTask<TrieNode> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(searchString, ft);
                if (f == null) { // this thread managed to update the cache and has to execute asynch the computeSearch() method
                    f = ft;
                    ft.run();
                }
            }
            else{
                System.out.println("Merge bine");
            }

            try {
                TrieNode node = null;
                try {
                    node = f.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Set<Integer> searchResult = new HashSet<>();
                if(node == null) {
                    cache.remove(searchString, f);// there is no reason to store a search result with a null value
                    //because meanwhile a user can be added and the cache is telling incorrectly that there is no result
                    return new HashSet<>();
                }
                //searchResult.addAll(node.authors.keySet());
                return node.authors.keySet();
            } catch (CancellationException e) {
                cache.remove(searchString, f);
            } catch (ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Collection<Author> convertIdsToAuthors(Collection<String> ids){
        List<Author> authors = new ArrayList<>();
        for (String id: ids){
            authors.add(authorsIds.get(id));
        }
        return authors;
    }
    
    @Override
    public Collection<Author> searchAuthorsByName(String queryString) {
        String[] names = queryString.split("[ -]");
        if(names.length == 0)
            return null;

        //return authorRepository.findAuthorsByFirstnameContaining(names[0]);

        Set<String> queryResult = new HashSet<>();

        queryResult.addAll(search(names[0]));
        for(int i = 1; i < names.length; i++){

            Set<String> currentNameResult = search(names[i]);
            queryResult.retainAll(currentNameResult);
        }
        return convertIdsToAuthors(queryResult);
    }


}
