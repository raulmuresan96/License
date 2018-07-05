package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Journal;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repo.JournalRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;
    private String tokenValue = "", journalApiUrl = "";


    public JournalService(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src/main/resources/config.info"));
            tokenValue = properties.getProperty("x-els-apikey");
            journalApiUrl = properties.getProperty("journalApiURL");
            //System.out.println(tokenValue + " " + journalApiUrl);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Journal findOne(String issn){
        return journalRepository.findOne(issn);
    }

    public Iterable<Journal> getAllJournals(){
        return journalRepository.findAll();
    }

    public Journal addJournal(Journal journal){
        return journalRepository.save(journal);
    }

    public void populateFromFile(){
        getJournalsFromFile();
        //journalRepository.save(new Journal("ass1", "A*"));
    }

    private Map<String, Integer> journalCategoryMap;
    private  Map<String, Set<String>> categoryMap;
    private  List<String> currentCategoryJournals;
    private  int currentCategory;

    private  String convertIntegerToCategory(int value) {
//        0 = A*
//        1 = A
//        2 = B
//        3 = C
        String category;
        if (value == 0) {
            category = "A*";
        } else {
            category = (char) (value + 'A' - 1) + "";
        }
        return category;
    }


    public  boolean isISSN(String string) {
        return string.matches("^[0-9X]{4}-[0-9X]{4}$");
    }

    public void updateBestCategory(String issn, int paperBestCategory) {
        //Checks if the currentCategory of a journal it's the best of its
        if (journalCategoryMap.get(issn) != null) {
            paperBestCategory = Math.min(paperBestCategory, journalCategoryMap.get(issn));
        }
        journalCategoryMap.put(issn, paperBestCategory);
    }

    public void evaluateCategory() {
        //If the category of the new journal is different from the old one, that means that a certain category
        //has finished and we begin to evaluate each journal of the current category

        int fivePercent = (int) (Math.ceil(5.0 * currentCategoryJournals.size() / 100));
//        System.out.println("New category " + currentCategory + " Size: " + currentCategoryJournals.size() + " " +
//                fivePercent);
        int n = currentCategoryJournals.size();

        for (int i = 0; i < fivePercent; i++) {
            String journal = currentCategoryJournals.get(i);
            updateBestCategory(journal, currentCategory - 1);

        }
        for (int i = fivePercent; i < n; i++) {
            String journal = currentCategoryJournals.get(i);
            updateBestCategory(journal, currentCategory);
        }

        currentCategoryJournals.clear();

    }

    public void evaluateJournal(List<String> journal) {
        //System.out.println(journal + " " + ++count);
        int n = journal.size();
        String issn = journal.get(n - 3);
        int journalCategory = Integer.parseInt(journal.get(n - 2));
        if (journalCategory != currentCategory) {
            evaluateCategory();
        }
        currentCategoryJournals.add(issn);
        currentCategory = journalCategory;
    }

    public void getDataForJournalFromScopus(String issn, String category){

        try {
            System.out.println(journalApiUrl + issn);
            URL url = new URL(journalApiUrl + issn);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("x-els-apikey", tokenValue);


            if (conn.getResponseCode() != 200) {
                System.out.println("Current issn not found on Scopus");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println("okay");
                //System.out.println(output);
                JsonParser jsonParser = new JsonParser();
                JsonObject jo = (JsonObject)jsonParser.parse(output);
                //System.out.println(jo.entrySet());
                String journaltitle = jo.get("serial-metadata-response").getAsJsonObject().get("entry").getAsJsonArray().get(0).getAsJsonObject()
                        .get("dc:title").getAsString();
                //journalRepository.save(new Journal(issn, category, journaltitle));
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getJournalsFromFile() {
        journalCategoryMap = new HashMap<>();
        currentCategoryJournals = new ArrayList<>();
        currentCategory = 1;
        String filename = "src/main/resources/citations.pdf";
        //citations.pdf
        //Working fine
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File(filename);
        try {
            PDFParser parser = new PDFParser(new FileInputStream(file));
            parser.parse();
            cosDoc = parser.getDocument();

            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            //int count = pdDoc.getNumberOfPages();
            List<String> currentJournal = new ArrayList<>();
            for (int nrPage = 1; nrPage <= pdDoc.getNumberOfPages(); nrPage++) {
//                System.out.println(nrPage);
                pdfStripper.setStartPage(nrPage);
                pdfStripper.setEndPage(nrPage);
                String parsedText = pdfStripper.getText(pdDoc);

                String[] lines = parsedText.split("\\r?\\n");
                for (int i = 6; i < lines.length - 1; i++) { //To Skip parsing the information about the document and
                    // current page number(last lines of a page)
                    String line = lines[i];
                    String[] words = line.split(" ");
                    for (int j = 0; j < words.length; j++) {
                        String word = words[j];
                        if (isISSN(word)) { // if a ISSN is found, the category and the rank are read and a journal is created
                            currentJournal.add(words[j]);
                            currentJournal.add(words[j + 1]);
                            currentJournal.add(words[j + 2]);
                            j += 2;
                            evaluateJournal(currentJournal);

                            currentJournal.clear();
                        } else {
                            currentJournal.add(word);
                        }
                    }
                }
            }
            //The last category has to be explicitly evaluated
            evaluateCategory();

            categoryMap = new HashMap<>();
            categoryMap.put("A*", new HashSet<>());
            categoryMap.put("A", new HashSet<>());
            categoryMap.put("B", new HashSet<>());
            categoryMap.put("C", new HashSet<>());

            journalCategoryMap.forEach((issn, integerCategory) -> {
                String category = convertIntegerToCategory(integerCategory);
                categoryMap.get(category).add(issn);
                journalRepository.save(new Journal(issn, category));
                System.out.println(issn + " " + category);
                //getDataForJournal(issn, convertIntegerToCategory(integerCategory));

            });

            //System.out.println("WORKDSSSSS");
            categoryMap.forEach((category, issn) -> {
                System.out.println(category + " " + issn.size() + " " + issn);
            });
            //getDataForJournal("0305-4403", convertIntegerToCategory(1));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            // TODO Auto-generated catch block
        }
    }
}
