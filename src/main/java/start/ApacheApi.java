package start;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Raul on 16/04/2018.
 */
public class ApacheApi {
    public static void main(String[] args) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://www.scopus.com/authid/detail.uri?authorId=14032958500");
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("x-els-apikey", "fa749f1f51a7cd468697c4ad217021ea");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            System.out.println(e.getCause());
            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }




}
