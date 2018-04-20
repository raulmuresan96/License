package controller;

import model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import service.AuthorService;
import service.InitService;
import service.JournalService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Raul on 25/11/2017.
 */

@RestController
@RequestMapping("/API/init")
public class InitController {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private JournalService journalService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private InitService service;

    @RequestMapping(method = RequestMethod.GET)
    public void init(){
        journalService.populateFromFile();
    }

    @RequestMapping(value = "/API/string", method = RequestMethod.GET)
    public List<String> getAllReportsByUser(){
        System.out.println("Ajunge la get " + Thread.currentThread().getName());

        taskExecutor.execute(() -> {
            try {
                Thread.sleep(6000);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        System.out.println("method returns, but thre created thread executes in background");
        return Arrays.asList("abc", "xyz", "qwerty");
    }


    @RequestMapping(value = "/API/pdf", method = RequestMethod.POST)
    public ResponseEntity<?> uploadPDF(@RequestBody byte[] pdfByteArray){
        System.out.println("ajungeeee");
        System.out.println(pdfByteArray.length);

        try {
            OutputStream out = new FileOutputStream("/Users/Raul/Documents/SpringBootExample/src/main/resources/citations.pdf");
            out.write(pdfByteArray);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return ResponseEntity.ok("File succesfully uploaded");
    }

    @RequestMapping(value = "/API/authors", method = RequestMethod.GET)
    public void addAuthors(){
        System.out.println("ajunge la adaugare de autori");
        authorService.init();
    }


    @RequestMapping(value = "author/{name}", method = RequestMethod.GET)
    public Collection<Author> deleteUser(@PathVariable("name")String queryString){
        return authorService.searchByQueryString(queryString);
    }


}
