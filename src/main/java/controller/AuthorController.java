package controller;

import model.Author;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.AuthorService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Raul on 07/05/2018.
 */

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")

public class AuthorController {

    @Autowired
    private AuthorService authorService;

    private AtomicLong atomicInteger = new AtomicLong(0);

    @RequestMapping(value = "author/{name}", method = RequestMethod.GET)
    public Collection<Author> searchAuthors(@PathVariable("name")String queryString){
        queryString = queryString.toLowerCase();
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

    private byte[] getByteArrayFromFile(String fileName){
        File fileToReturn = new File(fileName);
        Path path = Paths.get(fileToReturn.getAbsolutePath());
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource.getByteArray();
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)

    public ResponseEntity<byte[]> uploadPDF(MultipartFile publicationCsv, MultipartFile citationCsv){
        //public ResponseEntity<?> uploadPDF(@RequestBody byte[] pdfByteArray){
    //public List<byte[]> uploadPDF(MultipartFile file1, MultipartFile file2){
        //ResponseEntity<org.springframework.core.io.Resource>



//        File fileToReturn = new File(fileName);
//        Path path = Paths.get(fileToReturn.getAbsolutePath());
//        ByteArrayResource resource = null;
//        try {
//            resource = new ByteArrayResource(Files.readAllBytes(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //return resource.getByteArray();


        System.out.println(publicationCsv.getContentType());
        System.out.println(citationCsv.getContentType());
        //System.out.println(file1.getSize());
        //System.out.println(file2.getSize());


//        List<byte[]> list = new ArrayList<>();
//        byte[] bytes1 = getByteArrayFromFile("src/main/resources/FirstPDF.pdf");
//        byte[] bytes2 = getByteArrayFromFile("src/main/resources/SecondPDF.pdf");
//        System.out.println(bytes1.length + " " + bytes2.length);
//        list.add(bytes1);
//        list.add(bytes2);


        byte[] response = getByteArrayFromFile("src/main/resources/FirstPDF.pdf");
        //System.out.println(response.length);
//        list[0] = ;
//        list.add(resource);



//        JSONObject item = new JSONObject();
//        item.put("file1", resource.getByteArray());
        //item.put("file2", file2);




        //return list;
        return ResponseEntity.ok()

                //.headers(headers)
                //.contentLength(fileToReturn.length())\
                //.contentType(MediaType.parseMediaType("multipart/form-data"))
                .body(response);

//        return ResponseEntity.ok()
//                //.headers(headers)
//                .contentLength(fileToReturn.length())
//                .contentType(MediaType.parseMediaType("multipart/form-data"))
//                .body(resource);


        //return file1;
        //return new Author("123", "raul", "muresan");
    }

    @RequestMapping(value = "/string", method = RequestMethod.POST)
    public ResponseEntity<?> uploadPDF(@RequestBody String string){
        System.out.println("S-a primit :" + string);
        return ResponseEntity.ok("String was received by the server");
    }


}
