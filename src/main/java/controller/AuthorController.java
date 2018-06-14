package controller;

import model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AuthorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Raul on 07/05/2018.
 */

@RestController
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    private AtomicLong atomicInteger = new AtomicLong(0);

    @RequestMapping(value = "author/{name}", method = RequestMethod.GET)
    public Collection<Author> searchAuthors(@PathVariable("name")String queryString){
        long startTime = System.nanoTime();


        //System.out.println("ajunge la cautare de autori");
        Collection<Author> result = authorService.searchByQueryString(queryString);

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;;
        System.out.println("GET " + elapsedTime + " " + queryString + " " + result);
        return result;
    }


    @RequestMapping(value = "author/add", method = RequestMethod.POST)
    public Author add(@RequestBody Author author){
        long startTime = System.nanoTime();


        //System.out.println("ajunge la cautare de autori");
        Author result = authorService.save(author);

        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;;
        System.out.println("ADD " + elapsedTime + " " + result);
        return result;

        //return authorService.searchByQueryString(queryString);
    }


    @RequestMapping(value = "/API/authors", method = RequestMethod.GET)
    public void initializeAuthors(){
        System.out.println("ajunge la adaugare de autori");
        authorService.init();
    }


}
