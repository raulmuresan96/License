package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Author;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.AuthorTrieRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Raul on 17/04/2018.
 */
@Service
public class AuthorService {

    @Autowired
    private AuthorTrieRepository authorTrieRepository;
    private String tokenValue = "", authorSearchByNameApiUrl = "", authorsUrl = "";

    public AuthorService() {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/resources/config.info"));
            tokenValue = properties.getProperty("x-els-apikey");
            authorSearchByNameApiUrl = properties.getProperty("authorSearchByName");
            authorsUrl = properties.getProperty("UBBAuthorsUrl");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public  String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
            result.append("\n");
        }
        rd.close();
        //System.out.println(result.toString());
        return result.toString();
    }

    public  void searchAuthors(String string, String text, Set<String> authors){
        int index =  text.indexOf(string, 0);
        while(index >= 0) {
            //System.out.println(index);
            //System.out.println(text.substring(index, index + 20));
            StringBuilder authorName = new StringBuilder();
            index = index + string.length();
            while (index < text.length() && (Character.isLetter(text.charAt(index)) || text.charAt(index) == ' ' || text.charAt(index) == '.' || text.charAt(index) == '-')) {
                authorName.append(text.charAt(index));
                index++;
            }
            authors.add(authorName.toString().trim());
            index = text.indexOf(string, index);
        }
    }


    public String transformStringToAscii(String string) throws UnsupportedEncodingException {

        String s1 = Normalizer.normalize(string, Normalizer.Form.NFKD);
        String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

        return new String(s1.replaceAll(regex, "").getBytes("ascii"), "ascii");
    }

    public  void parseAuthorXml(String queryUrl, String repsponseText){
        //System.out.println(repsponseText);
        String authorId = queryUrl.substring(queryUrl.lastIndexOf('/') + 1);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(repsponseText);

        String surname = jsonObject.get("author-retrieval-response").getAsJsonArray().get(0).getAsJsonObject().get("author-profile").getAsJsonObject()
                .get("preferred-name").getAsJsonObject().get("surname").getAsString();

        String firstName = jsonObject.get("author-retrieval-response").getAsJsonArray().get(0).getAsJsonObject().get("author-profile").getAsJsonObject()
                .get("preferred-name").getAsJsonObject().get("given-name").getAsString();

        System.out.println(surname + " " +  firstName + " " +  authorId);
        try {
            Author author = new Author(transformStringToAscii(authorId), transformStringToAscii(surname), transformStringToAscii(firstName));
            this.save(author);
        }
        catch (UnsupportedEncodingException e){
            System.out.println("Normalizing error: " + e.getMessage());
        }

    }


    public  void searchAuthorById(String queryUrl){
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
            //HttpClient client = new HttpClient();
            HttpGet getRequest = new HttpGet(queryUrl);
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("x-els-apikey", tokenValue);

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Author not found on Scopus");
                return;
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            StringBuilder responseJson = new StringBuilder();
            while ((output = br.readLine()) != null) {
                responseJson.append(output);
                //System.out.println(output);
            }
            parseAuthorXml(queryUrl, responseJson.toString());

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void searchAuthorByName(String name){
        System.out.println(name);
        String[] names = name.split("[ -]");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 1; i < names.length; i++){
            stringBuilder.append(names[i]);
            stringBuilder.append("%20");
        }
        String surname = names[0];
        String firstName = stringBuilder.toString().trim();
//        query= AUTHLASTNAME(Mircea) AND AUTHFIRST(Gabriel Ioan) AND AFFIL(Universitatea Babes-Bolyai din Cluj-Napoca)
        //String apiUrl = authorSearchByNameApiUrl + "query=AUTHLASTNAME(" + surname + ")AND%20AUTHFIRST(" + firstName + ")AND%20AFFIL(Universitatea%20Babes-Bolyai%20din%20Cluj-Napoca)";
        String apiUrl = authorSearchByNameApiUrl + "query=AUTHLASTNAME(" + surname + ")AND AUTHFIRST(" + firstName + ")AND AFFIL(Universitatea Babes-Bolyai din Cluj-Napoca)";
        apiUrl = apiUrl.replaceAll(" ", "%20");


        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
            HttpGet getRequest = new HttpGet(apiUrl);
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("x-els-apikey", tokenValue);

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Author not found on Scopus");
                return;
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            StringBuilder responseJson = new StringBuilder();
            while ((output = br.readLine()) != null) {
                responseJson.append(output);
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject)jsonParser.parse(responseJson.toString());
            //System.out.println(jo.entrySet());
            JsonArray queryResult = jsonObject.get("search-results").getAsJsonObject().get("entry").getAsJsonArray();
            if(queryResult.size() == 1 && queryResult.get(0).getAsJsonObject().has("error")){
                return;
            }
            for(int i = 0; i < queryResult.size(); i++){
                String queryUrl = queryResult.get(i).getAsJsonObject().get("prism:url").getAsString();
                searchAuthorById(queryUrl);
            }
//            System.out.println(jo.get("serial-metadata-response").getAsJsonObject().get("entry").getAsJsonArray().get(0).getAsJsonObject()
//                    .get("dc:title").getAsString());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        try {
            Set<String> authors = new HashSet<>();
            String htmlText = getHTML(authorsUrl);
            searchAuthors("Dr.", htmlText, authors);
            searchAuthors("Drd.", htmlText, authors);

            //authors.forEach(System.out::println);
            authors.forEach(this::searchAuthorByName);
            //searchAuthorByName("Rusu Catalin");
            //searchAuthorByName("Mircea Ioan Gabriel");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Author save(Author author){
        return authorTrieRepository.save(author);
    }

    public Iterable<Author> findAll(){
        return authorTrieRepository.findAll();
    }

    public Collection<Author> searchByQueryString(String queryString){
        String[] names = queryString.split("[ -]");
        return authorTrieRepository.searchAuthors(names);
    }
}
