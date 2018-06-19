package repo;

import model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Created by Raul on 10/06/2018.
 */

@Repository("kmp")
public class AuthorRepositoryKMP implements IAuthorRepository {
    @Autowired
    private AuthorRepository authorRepository;
    private Map<Author, Boolean> authors;

    @PostConstruct
    public void init(){
        populateDB();
        authors = new ConcurrentHashMap<>();
        authorRepository.searchAuthors().forEach(author ->{
            authors.put(author, true);
        });
        System.out.println("Loading finished");
        //System.out.println(authors);
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




    public static boolean kmpSearch(String substringS, String stringS, int[] prefix) {
        char[] substring = substringS.toCharArray();
        char[] string = stringS.toCharArray();
        int M = substring.length;
        int N = string.length;

        int k = 0;
        for (int i = 1; i < N; i++) {
            while (k >= 0 && substring[k + 1] != string[i])
                k = prefix[k];
            k++;
            if (k == M - 1) {
                return true;
            }
        }
        return false;
    }




    public int[] computePrefixArray(String string) {
        char[] substring = string.toCharArray();
        int N = substring.length;
        int[] prefix = new int[N];
        prefix[0] = -1;
        int k = -1;

        for (int i = 1; i < N; i++) {
            while (k >= 0 && substring[k + 1] != substring[i])
                k = prefix[k];
            prefix[i] = ++k;
        }
        return prefix;
    }

    private Set<Author> searchAuthorsByWord(String searchString){
        System.out.println(searchString);
        searchString = " " + searchString;
        int[] prefix = computePrefixArray(searchString);
        System.out.println();
        Set<Author> searchResult = new HashSet<>();
        for(Author author: authors.keySet()){
            if(kmpSearch(searchString, " " + author.getFirstname(), prefix) || kmpSearch(searchString, " " + author.getSurname(), prefix)){
                searchResult.add(author);
            }
        }
        return searchResult;
    }

//    private Set<Author> searchAuthorsByWord(String queryString) {
//        Set<Author> searchResult = new HashSet<>();
//        for(Author author: authors.keySet()){
//            if(author.getSurname().contains(queryString) || author.getFirstname().contains(queryString) ){
//                searchResult.add(author);
//            }
//        }
//        return searchResult;
//    }

    @Override
    public Collection<Author> searchAuthorsByName(String queryString) {
        String[] names = queryString.split("[ -]");
        if(names.length == 0)
            return null;

        //return authorRepository.findAuthorsByFirstnameContaining(names[0]);

        Set<Author> queryResult = new HashSet<>();
        queryResult.addAll(searchAuthorsByWord(names[0]));
//        for(int i = 1; i < names.length; i++){
//
//            Set<Author> currentNameResult = searchAuthorsByWord(names[i]);
//            //System.out.println(currentNameResult);
//            queryResult.retainAll(currentNameResult);
//        }
        return queryResult;

    }

    @Override
    public Author addAuthor(Author author) {
        Author result = authorRepository.save(author);
        authors.put(author, true);
        return result;
    }
}
