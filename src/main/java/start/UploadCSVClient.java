package start;

import model.CitationPublicationCsv;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Raul on 14/06/2018.
 */
public class UploadCSVClient {
    public static void main(String[] args) {
        System.out.println("works");


        Path publicationCsv = Paths.get("src/main/resources/a.csv");
        Path citationCsv = Paths.get("src/main/resources/aa.csv");


        RestTemplate restTemplate = new RestTemplate();

//        try {
//            byte[] publication = Files.readAllBytes(publicationCsv);
//            byte[] citation = Files.readAllBytes(citationCsv);
//            System.out.println(publication.length);
//            System.out.println(citation.length);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try{
            byte[] publication = Files.readAllBytes(publicationCsv);
            byte[] citation = Files.readAllBytes(citationCsv);
            System.out.println(publication.length);
            System.out.println(citation.length);
            CitationPublicationCsv citationPublicationCsv = new CitationPublicationCsv(publication, citation);
                //restTemplate.pos
            //restTemplate.postForEntity()
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://localhost:8090/generatePdf", citationPublicationCsv, String.class);
            System.out.println(stringResponseEntity);
        }
        catch(RestClientException ex){
            ex.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
