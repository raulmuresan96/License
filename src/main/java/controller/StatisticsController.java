package controller;

import model.CitationPublicationCsv;
import model.Journal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.JournalService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raul on 14/06/2018.
 */

@RestController
public class StatisticsController {
    @Autowired
    private JournalService journalService;

    private String publicationFileName = "src/main/resources/pub.csv";
    private String citationFileName = "src/main/resources/cit.csv";
    private String pythonFile = "src/main/java/start/generateScientificProduction.py";
    private List<String> categoryAPapers = new ArrayList<>();
    private List<String> categoryBPapers = new ArrayList<>();
    private List<String> categoryCPapers = new ArrayList<>();
    private List<Integer> categoryAPoints = new ArrayList<>();
    private List<Integer> categoryBPoints = new ArrayList<>();
    private List<Integer> categoryCPoints = new ArrayList<>();




    private void saveCsvFiles(CitationPublicationCsv citationPublicationCsv){
        try {
            OutputStream out = new FileOutputStream(publicationFileName);
            out.write(citationPublicationCsv.getPublicationCsv());
            out.close();

            out = new FileOutputStream(citationFileName);
            out.write(citationPublicationCsv.getCitationCsv());
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminateLastSpecificCharacter(StringBuilder stringBuilder){
        if(stringBuilder.length() > 0){
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        else{
            stringBuilder.append("[]");
        }
    }


    private void generateScientificProductionPDF(List<String>  publicationLineList){
        //StringBuilder publicationInformation = new StringBuilder();
        getPublicationDataForPrinting(publicationLineList.size() - 1);

        List<String> pythonScriptsArguments = new ArrayList<>();

        StringBuilder categoryAString = new StringBuilder();
        categoryAPapers.forEach(publication -> categoryAString.append(publication).append("#"));
        eliminateLastSpecificCharacter(categoryAString);
        //categoryAString.deleteCharAt(categoryAString.length() - 1);

        StringBuilder categoryBString = new StringBuilder();
        categoryBPapers.forEach(publication -> categoryBString.append(publication).append("#"));
        //categoryBString.deleteCharAt(categoryBString.length() - 1);
        eliminateLastSpecificCharacter(categoryBString);

        StringBuilder categoryCString = new StringBuilder();
        categoryCPapers.forEach(publication -> categoryCString.append(publication).append("#"));
        //categoryCString.deleteCharAt(categoryCString.length() - 1);
        eliminateLastSpecificCharacter(categoryCString);

        pythonScriptsArguments.add(categoryAString.toString().replace(' ', '$'));
        pythonScriptsArguments.add(categoryAPoints.toString().replace(' ', '$'));

        pythonScriptsArguments.add(categoryBString.toString().replace(' ', '$'));
        pythonScriptsArguments.add(categoryBPoints.toString().replace(' ', '$'));

        pythonScriptsArguments.add(categoryCString.toString().replace(' ', '$'));
        pythonScriptsArguments.add(categoryCPoints.toString().replace(' ', '$'));

        executePythonScript(pythonFile, pythonScriptsArguments);


//        System.out.println(categoryAPapers);
//        System.out.println(categoryAPoints);

        System.out.println(pythonScriptsArguments);

        //String categoryA


        //.forEach(publication -> publicationInformation.append(publication).append("#"));



//        getPublicationDataForPrinting(publicationLineList.size() - 1).forEach(publication -> publicationInformation.append(publication).append("#"));
//        publicationInformation.deleteCharAt(publicationInformation.length() - 1);
//        System.out.println(publicationInformation);
//        executePythonScript(pythonFile, publicationInformation.toString().replace(' ', '$'));
    }

    @RequestMapping(value = "/generatePdf", method = RequestMethod.POST)
    public ResponseEntity<?> init(@RequestBody CitationPublicationCsv citationPublicationCsv){
//        System.out.println(citationPublicationCsv.getPublicationCsv().length);
//        System.out.println(citationPublicationCsv.getCitationCsv().length);

        saveCsvFiles(citationPublicationCsv);

        List<String> publicationLineList = readCSVFile(publicationFileName);
        List<String> citationLineList = readCSVFile(citationFileName);

        List<String> publicationLabels = parseLineFromCSV(publicationLineList.get(0));
        List<String> citationLabels = parseLineFromCSV(citationLineList.get(0));

        publicationsMap = buildMap(publicationLabels, publicationLineList);
        citationsMap = buildMap(citationLabels, citationLineList);

        generateScientificProductionPDF(publicationLineList);



        return ResponseEntity.ok("File succesfully uploaded");
        //return (ResponseEntity<?>) ResponseEntity.badRequest();
        //journalService.populateFromFile();
    }


//     uploadPDF(@RequestBody byte[] pdfByteArray){
//        System.out.println("ajungeeee");
//        System.out.println(pdfByteArray.length);
//
//        try {
//            OutputStream out = new FileOutputStream("/Users/Raul/Documents/SpringBootExample/src/main/resources/citations.pdf");
//            out.write(pdfByteArray);
//            out.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return ResponseEntity.ok("File succesfully uploaded");


     Map<String, List<String>> publicationsMap;
     Map<String, List<String>> citationsMap;
     Map<Integer, List<String>> citedPublications;

    private  List<String> parseLineFromCSV(String line){
        int index = 0;
        StringBuilder currentString = new StringBuilder();
        List<String> words = new ArrayList<>();
        while(index < line.length()){
            if(line.charAt(index) == '"'){
                index++;
                //while(index < line.length() && (line.charAt(index) != '"' && (index + 1 == line.length() || line.charAt(index + 1) == ','))){
                while(index < line.length() && line.charAt(index) != '"' ){
                    currentString.append(line.charAt(index));
                    index++;
                }
                index = index + 2;
            }
            else{
                //index++;
                while(index < line.length() && line.charAt(index) != ','){
                    currentString.append(line.charAt(index));
                    index++;
                }
                index++;

            }
            String word = currentString.toString().trim();
            //word = word.replaceAll("\u00A0", "");
            //System.out.println((int)word.charAt(0));
            //System.out.println(word);
            words.add(word);
            currentString = new StringBuilder();
        }
        return words;
    }

    public  List<String> readCSVFile(String csvFile){
        List<String> lineList = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                //System.out.println(line.trim());
                // use comma as separator

                lineList.add(line.replaceAll("\"\"", ""));
                //parseLineFromCSV(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineList;
    }

    public  List<String> getPublicationDataForPrinting(int nrPublications){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < nrPublications; i++){
            String issn = publicationsMap.get("ISSN").get(i);
            if(issn.length() > 0){
                issn = issn.substring(0, 4) + "-" + issn.substring(4);

                Journal journal = journalService.findOne(issn);
                if(journal != null){
                    String journalCategory = journal.getCategory();
                    if(journalCategory.equals("A") || journalCategory.equals("A*")){
                        categoryAPapers.add(publicationsMap.get("\uFEFFAuthors").get(i) + "," + publicationsMap.get("Title").get(i) + "," + publicationsMap.get("Source title").get(i));
                        categoryAPoints.add(3);
                    }
                    else if(journalCategory.equals("B")){
                        categoryBPapers.add(publicationsMap.get("\uFEFFAuthors").get(i) + "," + publicationsMap.get("Title").get(i) + "," + publicationsMap.get("Source title").get(i));
                        categoryBPoints.add(2);
                    }
                    else {//Category C
                        categoryCPapers.add(publicationsMap.get("\uFEFFAuthors").get(i) + "," + publicationsMap.get("Title").get(i) + "," + publicationsMap.get("Source title").get(i));
                        categoryCPoints.add(1);
                    }
                    //System.out.println("Categorie " + );
                    //System.out.println(journal.getCategory() + " " + publicationsMap.get("Title").get(i));
                }
            }

            //list.add(publicationsMap.get("\uFEFFAuthors").get(i) + "," + publicationsMap.get("Title").get(i) + "," + publicationsMap.get("Source title").get(i));
            //System.out.println(publicationsMap.get("Title").get(i) + "####" + publicationsMap.get("Source title").get(i));
        }
        return list;
    }


    public  void executePythonScript(String fileName, List<String> arguments){
        try {
            Process p = null;
            StringBuilder command = new StringBuilder("python ");
            command.append(fileName);

            for (String argument : arguments) {
                command.append(" ");
                command.append(argument);
            }
            //command = command + fileName + " " + publicationInformation;
            System.out.println(command);
            p = Runtime.getRuntime().exec(command.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String input = "";

            while ((input = in.readLine()) != null) {
                System.out.println(input);
            }

            BufferedReader errorInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((input = errorInput.readLine()) != null) {
                System.out.println("Error " + input);
            }

            p.destroy();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //se returneaza un map care contine ca si cheie numele unui camp, iar ca valoare o lista cu
    //informatiile publicatiilor corespunzatoare campului din cheie
    public  Map<String, List<String>> buildMap(List<String> publicationLabels, List<String> lineList){
        Map<String, List<String>> map = new HashMap<>();

        publicationLabels.forEach((entity) ->{
            map.put(entity, new ArrayList<>());
        });
        //System.out.println(publicationLabels);
        for(int i = 1; i < lineList.size(); i++){
            List<String> fields = parseLineFromCSV(lineList.get(i));
            //System.out.println(fields);
            for(int j = 0; j < publicationLabels.size(); j++){
                map.get(publicationLabels.get(j)).add(fields.get(j));
                //System.out.println(publicationLabels.get(j) + "   " + fields.get(j));
            }
        }
        return map;
    }

    private  Map<Integer, List<String>> getAllCitedPublications() {
        System.out.println();
        Map<Integer, List<String>> map = new HashMap<>();
        List<String> citations = publicationsMap.get("Cited by");
        for(int i = 0; i < citations.size(); i++){
            if(citations.get(i).length() > 0){
                //System.out.println(publicationsMap.get("Title").get(i));
                map.put(i, new ArrayList<>());
            }
        }
        return map;
    }

    private  void populateCitedPublicationsMap() {

        for(int currentPublication: citedPublications.keySet()){
            //System.out.println(currentPublication);
            String title = publicationsMap.get("Title").get(currentPublication);
            System.out.println(title);

            for(int i = 0; i < citationsMap.get("References").size(); i++){
                if(citationsMap.get("References").get(i).contains(title)){
                    System.out.println("Found match ");
                    citedPublications.get(currentPublication).add(citationsMap.get("Title").get(i));
                }
            }
            //break;
        }

    }




    public  void main(String[] args) {
        String csvPublicationFile = "src/main/resources/publication.csv";
        String csvCitationFile = "src/main/resources/citation.csv";


        List<String> publicationLineList = readCSVFile(csvPublicationFile);
        List<String> citationLineList = readCSVFile(csvCitationFile);

        List<String> publicationLabels = parseLineFromCSV(publicationLineList.get(0));
        List<String> citationLabels = parseLineFromCSV(citationLineList.get(0));

        publicationsMap = buildMap(publicationLabels, publicationLineList);
        citationsMap = buildMap(citationLabels, citationLineList);

        StringBuilder publicationInformation = new StringBuilder();
        getPublicationDataForPrinting(publicationLineList.size() - 1).forEach(publication -> publicationInformation.append(publication).append("#"));
        publicationInformation.deleteCharAt(publicationInformation.length() - 1);
        System.out.println(publicationInformation);
        //executePythonScript(pythonFile, publicationInformation.toString().replace(' ', '$'));

        citedPublications = getAllCitedPublications();

        populateCitedPublicationsMap();

        //System.out.println(citationsMap.get("References").get(0));
        for(String reference: citationsMap.get("References")){
            //System.out.println(reference);
        }


        System.out.println();

        citationsMap.get("References").forEach(System.out::println);
        System.out.println(citationsMap.get("References"));
        citedPublications.forEach((key, value) ->{
            System.out.println(publicationsMap.get("Title").get(key) + " " + value);
        });
        System.out.println(citationsMap.get("References").get(144).length());
        //System.out.println(citedPublications);
        //System.out.println(publicationsMap);
        System.out.println(citedPublications);
    }





}