package start;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Raul on 12/06/2018.
 */
public class AbstractApiCall {

    public static String apiCall(String queryUrl, String tokenValue){

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
            //HttpClient client = new HttpClient();
            HttpGet getRequest = new HttpGet(queryUrl);
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("x-els-apikey", tokenValue);

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Not Found Error");
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            StringBuilder responseText = new StringBuilder();
            while ((output = br.readLine()) != null) {
                responseText.append(output);
            }
            return responseText.toString();
            //parseAuthorXml(queryUrl, responseJson.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        String tokenValue = "";
        try {
            properties.load(new FileReader("src/main/resources/config.info"));
            tokenValue = properties.getProperty("x-els-apikey");
//            authorSearchByNameApiUrl = properties.getProperty("authorSearchByName");
//            authorsUrl = properties.getProperty("UBBAuthorsUrl");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //String queryUrl = "https://api.elsevier.com/content/abstract/eid/2-s2.0-85031413358";
        String queryUrl = "https://api.elsevier.com/content/abstract/eid/2-s2.0-84964692559";
        //2-s2.0-84964692559
        String responseText = apiCall(queryUrl, tokenValue);
        //System.out.println(responseText);


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(responseText);
//        System.out.println(jsonObject.get("abstracts-retrieval-response").getAsJsonObject().get("coredata").getAsJsonObject()
//        .get("dc:title"));
        System.out.println(jsonObject.get("abstracts-retrieval-response").getAsJsonObject().get("authors").getAsJsonObject()
        .get("author").getAsJsonArray().size());


        JsonArray authorArray = jsonObject.get("abstracts-retrieval-response").getAsJsonObject().get("authors").getAsJsonObject()
                .get("author").getAsJsonArray();

        for(int i = 0; i < authorArray.size(); i++){
            System.out.println(authorArray.get(i).getAsJsonObject().get("ce:indexed-name"));
        }
//        String surname = jsonObject.get("abstracts-retrieval-response").getAsJsonArray().get(0).getAsJsonObject().get("author-profile").getAsJsonObject()
//                .get("preferred-name").getAsJsonObject().get("surname").getAsString();
    }
}
