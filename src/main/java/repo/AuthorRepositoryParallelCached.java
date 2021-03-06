package repo;

import model.Author;
import model.CacheCell;
import model.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * Created by Raul on 08/05/2018.
 */
@Repository("parallelCached")
public class AuthorRepositoryParallelCached implements IAuthorRepository {
    @Autowired
    private AuthorRepository authorRepository;
    private Map<Author, Boolean> authors;
    private Map<String, Author> authorsIds;
    private final ConcurrentMap<String, Future<CacheCell>> cache = new ConcurrentHashMap<>();

//    private Map<String, Boolean> users;
    private TreeMap<Integer, Operation> operations;
    private ReadWriteLock treeMapReadWriteLock = new ReentrantReadWriteLock();
    private AtomicInteger operationCounter;

    //@PostConstruct
    public void init(){
        populateDB();
        authors = new ConcurrentHashMap<>();
        authorsIds = new ConcurrentHashMap<>();

        authorRepository.searchAuthors().forEach(author ->{
            authors.put(author, true);
            authorsIds.put(author.getAuthorId(), author);
        });
        operationCounter = new AtomicInteger(0);
        operations = new TreeMap<>();


        ExecutorService cacheCleaningExecutorService =  Executors.newSingleThreadExecutor();
        cacheCleaningExecutorService.submit(()->{
            try {
                while(true){
                    //System.out.println(date.getTime());
                    long startTime = new Date().getTime();
                    System.out.println("Iterating Cache Size: " + cache.size());
                    for(Map.Entry<String, Future<CacheCell>> entry: cache.entrySet()){

                        String key = entry.getKey();
                        Future<CacheCell> value = entry.getValue();
                        if(value.isDone()){
                            try {
                                CacheCell cacheCell = value.get();
                                //System.out.println("CACHE: " + key + " " +cacheCell.getUsers() + " " + cache.size());
                                //System.out.println(new Date().getTime() - cacheCell.getLastTimeQueried());
                                //System.out.println(startTime - cacheCell.getLastTimeQueried());
                                if(startTime - cacheCell.getLastTimeQueried() > 8000){
                                    //the remove operation can fail if meanwhile a query for the key was executed
                                    //and the removal is no longer needed
                                    cache.remove(key, value);
                                    //System.out.println(cache.remove(key, value));
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    Thread.sleep(10000);

                }
            }
            catch (InterruptedException exception){
                System.out.println("");
            }

            System.out.println("works fine");
        });

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

    @Override
    public Author addAuthor(Author author) {
//        if(authors.containsKey(author))
//            return null;
        System.out.println("Add " + author);
        authorRepository.save(author);
        authors.put(author, true);
        authorsIds.put(author.getAuthorId(), author);

        treeMapReadWriteLock.writeLock().lock();

        Operation operation = new Operation("Add", author);

        operations.put(operationCounter.incrementAndGet(), operation);
        treeMapReadWriteLock.writeLock().unlock();
        return author;
    }

    private CacheCell computeSearch(String searchString){
        int currentUpdate = operationCounter.get();
        Set<String> searchResult = new HashSet<>();
        for(Author author: authors.keySet()){
            //Author author = entry.getValue();
            if(author.getFirstname().contains(searchString) || author.getSurname().contains(searchString) ){
                searchResult.add(author.getAuthorId());
            }
        }
        return new CacheCell(currentUpdate, searchResult, new Date().getTime());
    }

    //CacheCell contains the moment when the last update was made
    //The TreeMap contains all the operations made sorted by the execution time
    //The TreeMap returns all the operations from the previous update and the cache cell is updated properly by checking all
    //operations done  meanwhile
    public CacheCell computeSearch(String searchString, CacheCell cacheCell){
        //System.out.println("Update Cache");
        Set<String> authors = new HashSet<>(cacheCell.getUsers());
        int currentOperationCount;
        treeMapReadWriteLock.readLock().lock();
        currentOperationCount = operationCounter.get();
        for(Operation operation: operations.tailMap(cacheCell.getLastUpdate(), false).values()){
            Author author = operation.getAuthor();
            String firstName = author.getFirstname();
            String surname = author.getSurname();
            if(operation.getType().equals("Add") && (firstName.contains(searchString) || surname.contains(searchString))){
                authors.add(author.getAuthorId());
            }
        }
        treeMapReadWriteLock.readLock().unlock();

        return new CacheCell(currentOperationCount, authors, new Date().getTime());
    }


    private Set<String> search(String searchString) throws InterruptedException {
        System.out.println("Search: " + searchString);
        while (true) {
            Future<CacheCell> f = cache.get(searchString);
            if (f == null) {
                Callable<CacheCell> eval = () -> computeSearch(searchString);

                FutureTask<CacheCell> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(searchString, ft);
                if (f == null) { // this thread managed to update the cache and has to execute asynch the computeSearch() method
                    f = ft;
                    ft.run();
                }
            }
            else{
                System.out.println("Update Cache " + searchString);
                try {

                    CacheCell cacheCell = f.get();
                    Callable<CacheCell> eval = () -> computeSearch(searchString, cacheCell);
                    FutureTask<CacheCell> ft = new FutureTask<>(eval);
                    boolean didModify = cache.replace(searchString, f, ft);
                    if(didModify){
                        f = ft;
                        ft.run();
                    }
                    //cache.put(searchString, ft);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            try {
                //CacheCell cacheCell = f.get();

                Future<CacheCell> cacheCellFuture = cache.get(searchString);
                //CacheCell cacheCell = cache.get(searchString).get();
                if(cacheCellFuture == null){
                    System.out.println("CACHE REMOVING WORKS");
                    //throw  new CancellationException("kfjnf");
                    return null;
                }

                return cacheCellFuture.get().getUsers();
            } catch (CancellationException e) {
                cache.remove(searchString, f);
            } catch (ExecutionException e) {
                System.out.println(e.getMessage());
            }
//            try {
//                CacheCell cacheCell = null;
//                try {
//                    cacheCell = f.get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                finally {
//                    return cacheCell.getUsers();
//                }
//
//
//            } catch (CancellationException e) {
//                cache.remove(searchString, f);
//            }
//            catch (ExecutionException e) {
//                System.out.println(e.getMessage());
//            }
        }
    }

    public Collection<Author> convertIdsToAuthors(Collection<String> ids){
        List<Author> authorsList = new ArrayList<>();
        for (String id: ids){
            authorsList.add(authorsIds.get(id));
        }
        return authorsList;
    }

    @Override
    public Collection<Author> searchAuthorsByName(String queryString) {
        String[] names = queryString.split("[ -]");
        if(names.length == 0)
            return null;

        //return authorRepository.findAuthorsByFirstnameContaining(names[0]);

        Set<String> queryResult = new HashSet<>();

        try {
            queryResult.addAll(search(names[0]));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 1; i < names.length; i++){

            Set<String> currentNameResult = null;
            try {
                currentNameResult = search(names[i]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queryResult.retainAll(currentNameResult);
        }
        return convertIdsToAuthors(queryResult);
    }

}
