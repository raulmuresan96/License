package start;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import service.JournalService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Raul on 16/04/2018.
 */

@ComponentScan(basePackages={"controller","service","configuration"})
public class APIJournalCall {


    @Autowired
    private static JournalService journalService;

    public static void main(String[] args) {
        System.out.println("merge bine");
        try {
            URL url = new URL("https://api.elsevier.com/content/serial/title/issn/0305-4403");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("x-els-apikey", "fa749f1f51a7cd468697c4ad217021ea");


            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println("okay");
                //System.out.println(output);
                JsonParser jsonParser = new JsonParser();
                JsonObject jo = (JsonObject)jsonParser.parse(output);
                //System.out.println(jo.entrySet());

//                System.out.println(jo.get("serial-metadata-response").getAsJsonObject().get("entry").getAsJsonArray().get(0).getAsJsonObject()
//                .get("dc:title"));
            }
            System.out.println(journalService);

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
