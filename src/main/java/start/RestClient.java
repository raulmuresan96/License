package start;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Raul on 10/04/2018.
 */
public class RestClient {

    public static void main(String[] args) {

//        for(int i = 0; i < 6; i++){
//            Runnable task = () -> {
//                RestTemplate restTemplate = new RestTemplate();
//                restTemplate.post
//                try{
//                    ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:8080/API/init/API/string", List.class);
//                    List<String> list = (List<String>)response.getBody();
//                    System.out.println(list);
//                }
//                catch(RestClientException ex){
//                    System.out.println("Exception ... "+ex.getMessage());
//                }
//            };
//            Thread thread = new Thread(task);
//            thread.start();
//        }

        Path pdfPath = Paths.get("/Users/Raul/Documents/SpringBootExample/src/main/resources/in.pdf");


        RestTemplate restTemplate = new RestTemplate();

                try{
                    byte[] pdf = Files.readAllBytes(pdfPath);
                    restTemplate.postForEntity("http://localhost:8080/API/init/API/pdf", pdf, byte[].class);
                }
                catch(RestClientException ex){
                    System.out.println("Exception ... "+ex.getMessage());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


    }
}
