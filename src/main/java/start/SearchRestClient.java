package start;

import model.Author;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import repo.AuthorRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Raul on 07/05/2018.
 */
public class SearchRestClient {

    public static List<String> readQueryFile(){
        String queryFileName = "src/main/resources/query.txt";
        List<String> queries = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(queryFileName), Charset.defaultCharset())) {
            //nrQueries = (int)lines.count();
            queries = lines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;
    }

    public static void populateFiles(){
        int usersCount = 10_000;
        int queriesCount = 100_000;
        RandomGenerator.populateFiles(usersCount,queriesCount);
    }


    public static void populateDB(){
        String fileName = "src/main/resources/users.txt";
        List<Author> authors = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line ->{
                String[] splitResult = line.split(" ");
                authors.add(new Author(splitResult[0], splitResult[1], splitResult[2]));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(authors.size());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8080/API/init/populate", authors, Object.class);
    }


    public static void executeQueries(){

        List<String> queries = readQueryFile();
        int nrQueries = queries.size();
        //System.out.println(nrQueries);

        int nrThreads = 8;
        int nrQueriesPerThread = nrQueries / nrThreads;
        AtomicInteger nr = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();

        long startTime = System.nanoTime();


        //int nrThreads = 8;
        for(int j = 0; j < nrThreads; j++){
            Runnable task = () -> {
                RestTemplate restTemplate = new RestTemplate();
                int left = nr.get();
                int right = nr.addAndGet(nrQueriesPerThread);
                System.out.println(left  + " " + right);
                for(int i = left; i < right; i++){
                    //restTemplate.post
                    try{
                        String currentQuery = queries.get(i);
                        //System.out.println(currentQuery);
                        String[] line = currentQuery.split(" ");
                        if(line[0].equals("ADD")){
                            //System.out.println("ADD");
                            Author author = new Author(line[1], line[2], line[3]);
                            //System.out.println("ADD " + author);
                            restTemplate.postForObject("http://localhost:8080/author/add", author, Author.class);
                            //searchEngine.addUser(line[1]);
                        }
                        else {
                            //System.out.println("Search " + line[1]);
                            ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:8080/author/"
                                    + line[1], List.class);
                            List<String> list = (List<String>)response.getBody();
                            //System.out.println("Search " + line[1] + list);
                            //System.out.println(line[1] + " " + searchEngine.search(line[1]));

                        }
                        //author = restTemplate.postForObject("http://localhost:8080/author/add", author, Author.class);

                        //Author author = new Author("1" , "Raul" ,"Muresan");
                        //author = restTemplate.postForObject("http://localhost:8080/author/add", author, Author.class);
                        //System.out.println(author);


                        //"http://localhost:8080/populate"

                        //ResponseEntity<String> response = restTemplate.put();
                        //ResponseEntity<List> response = restTemplate.getForEntity(, List.class);
                        //List<String> list = (List<String>)response.getBody();
                        //System.out.println(list);
                    }
                    catch(RestClientException ex){
                        System.out.println("Exception ... "+ex.getMessage());
                    }
                }

            };
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        for(int j = 0; j < nrThreads; j++){
            try {
                threads.get(j).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);
    }


    public static void main(String[] args) {




//        Logger logger = LogManager.getRootLogger();
//        logger.removeAppender("CONSOLE"); // stops console logging

        //System.out.println("Search " + );
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:8080/author/xuu", List.class);
//        List<String> list = (List<String>)response.getBody();
//        System.out.println(list);
        //populateFiles();
        //populateDB();
        executeQueries();


        //int nrThreads = 8;
//        for(int j = 0; j < 1000; j++){
//            System.out.println(j);
//            Runnable task = () -> {
//                RestTemplate restTemplate = new RestTemplate();
//
//                    try{
//                        //System.out.println("Search " + line[1]);
//                        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/API/init/connection" , String.class);
//                        String list = (String)response.getBody();
//                        System.out.println(list);
//                    }
//                    catch(RestClientException ex){
//                        System.out.println("Exception ... "+ex.getMessage());
//                    }
//                };
//
//            Thread thread = new Thread(task);
//            thread.start();
//        }

//        for(int j = 0; j < nrThreads; j++){
//            try {
//                threads.get(j).join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        long stopTime = System.currentTimeMillis();
//        long elapsedTime = stopTime - startTime;
//        System.out.println(elapsedTime);



    }
}
