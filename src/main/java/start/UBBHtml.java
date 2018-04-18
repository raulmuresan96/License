package start;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.internal.xsom.impl.util.Uri;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.bar;
import static com.sun.tools.doclets.formats.html.markup.HtmlStyle.sourceContainer;

/**
 * Created by Raul on 11/04/2018.
 */
public class UBBHtml {
    public static void main(String[] args) {
//        try {
//            String name = "Səcərea Christian";
//            String s1 = Normalizer.normalize(name, Normalizer.Form.NFKD);
//            String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";
//
//            String s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"), "ascii");
//            System.out.println(s2);
//
//        } catch (UnsupportedEncodingException e) {
//        }

        String string = "Səcərea Christian";
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        System.out.println(string);

        //string = string.replaceAll("[^\\p{ASCII}]", "");
    }

}
