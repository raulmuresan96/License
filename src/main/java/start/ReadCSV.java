package start;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Raul on 13/06/2018.
 */
public class ReadCSV {

//    static Map<String, List<String>> publicationsMap;
//    static Map<String, List<String>> citationsMap;
//    static Map<Integer, List<String>> citedPublications;
//
//    private static List<String> parseLineFromCSV(String line){
//        int index = 0;
//        StringBuilder currentString = new StringBuilder();
//        List<String> words = new ArrayList<>();
//        while(index < line.length()){
//            if(line.charAt(index) == '"'){
//                index++;
//                //while(index < line.length() && (line.charAt(index) != '"' && (index + 1 == line.length() || line.charAt(index + 1) == ','))){
//                while(index < line.length() && line.charAt(index) != '"' ){
//                    currentString.append(line.charAt(index));
//                    index++;
//                }
//                index = index + 2;
//            }
//            else{
//                //index++;
//                while(index < line.length() && line.charAt(index) != ','){
//                    currentString.append(line.charAt(index));
//                    index++;
//                }
//                index++;
//
//            }
//            String word = currentString.toString().trim();
//            //word = word.replaceAll("\u00A0", "");
//            //System.out.println((int)word.charAt(0));
//            //System.out.println(word);
//            words.add(word);
//            currentString = new StringBuilder();
//        }
//        return words;
//    }
//
//    public static List<String> readCSVFile(String csvFile){
//        List<String> lineList = new ArrayList<>();
//        String line;
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
//
//            while ((line = br.readLine()) != null) {
//                //System.out.println(line.trim());
//                // use comma as separator
//
//                lineList.add(line.replaceAll("\"\"", ""));
//                //parseLineFromCSV(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return lineList;
//    }
//
//    public static List<String> getPublicationDataForPrinting(int nrPublications){
//        List<String> list = new ArrayList<>();
//        for(int i = 0; i < nrPublications; i++){
//            list.add(publicationsMap.get("\uFEFFAuthors").get(i) + "," + publicationsMap.get("Title").get(i) + "," + publicationsMap.get("Source title").get(i));
//            //System.out.println(publicationsMap.get("Title").get(i) + "####" + publicationsMap.get("Source title").get(i));
//        }
//        return list;
//    }
//
//
//    public static void executePythonScript(String fileName, String publicationInformation){
//        try {
//            Process p = null;
//            String command = "python ";
//            command = command + fileName + " " + publicationInformation;
//            p = Runtime.getRuntime().exec(command);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String input = "";
//
//            while ((input = in.readLine()) != null) {
//                System.out.println(input);
//            }
//
//
//            BufferedReader errorInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//
//            while ((input = errorInput.readLine()) != null) {
//                System.out.println("Error " + input);
//            }
//
//            p.destroy();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //se returneaza un map care contine ca si cheie numele unui camp, iar ca valoare o lista cu
//    //informatiile publicatiilor corespunzatoare campului din cheie
//    public static Map<String, List<String>> buildMap(List<String> publicationLabels, List<String> lineList){
//        Map<String, List<String>> map = new HashMap<>();
//
//        publicationLabels.forEach((entity) ->{
//            map.put(entity, new ArrayList<>());
//        });
//        //System.out.println(publicationLabels);
//        for(int i = 1; i < lineList.size(); i++){
//            List<String> fields = parseLineFromCSV(lineList.get(i));
//            //System.out.println(fields);
//            for(int j = 0; j < publicationLabels.size(); j++){
//                map.get(publicationLabels.get(j)).add(fields.get(j));
//                //System.out.println(publicationLabels.get(j) + "   " + fields.get(j));
//            }
//        }
//        return map;
//    }
//
//    private static Map<Integer, List<String>> getAllCitedPublications() {
//        System.out.println();
//        Map<Integer, List<String>> map = new HashMap<>();
//        List<String> citations = publicationsMap.get("Cited by");
//        for(int i = 0; i < citations.size(); i++){
//            if(citations.get(i).length() > 0){
//                //System.out.println(publicationsMap.get("Title").get(i));
//                map.put(i, new ArrayList<>());
//            }
//        }
//        return map;
//    }
//
//    private static void populateCitedPublicationsMap() {
//
//        for(int currentPublication: citedPublications.keySet()){
//            //System.out.println(currentPublication);
//            String title = publicationsMap.get("Title").get(currentPublication);
//            System.out.println(title);
//
//            for(int i = 0; i < citationsMap.get("References").size(); i++){
//                if(citationsMap.get("References").get(i).toLowerCase().contains(title.toLowerCase())){
//                    System.out.println("Found match " + citationsMap.get("ISSN").get(i).length());
//                    citedPublications.get(currentPublication).add(citationsMap.get("Title").get(i));
//                }
//            }
//
//        }
//    }
//
//    public static void main(String[] args) {
//        String csvPublicationFile = "src/main/resources/publication.csv";
//        String csvCitationFile = "src/main/resources/citation.csv";
//        String pythonFile = "src/main/java/start/generateScientificProduction.py";
//
//        List<String> publicationLineList = readCSVFile(csvPublicationFile);
//        List<String> citationLineList = readCSVFile(csvCitationFile);
//
//        List<String> publicationLabels = parseLineFromCSV(publicationLineList.get(0));
//        List<String> citationLabels = parseLineFromCSV(citationLineList.get(0));
//
//        publicationsMap = buildMap(publicationLabels, publicationLineList);
//        citationsMap = buildMap(citationLabels, citationLineList);
//
//        StringBuilder publicationInformation = new StringBuilder();
//        getPublicationDataForPrinting(publicationLineList.size() - 1).forEach(publication -> publicationInformation.append(publication).append("#"));
//        publicationInformation.deleteCharAt(publicationInformation.length() - 1);
//        System.out.println(publicationInformation);
//        //executePythonScript(pythonFile, publicationInformation.toString().replace(' ', '$'));
//
//        citedPublications = getAllCitedPublications();
//        populateCitedPublicationsMap();
//
////        //System.out.println(citationsMap.get("References").get(0));
////        for(String reference: citationsMap.get("References")){
////            //System.out.println(reference);
////        }
////
////        citationsMap.get("References").forEach(System.out::println);
////        System.out.println(citationsMap.get("References"));
////        citedPublications.forEach((key, value) ->{
////            System.out.println(publicationsMap.get("Title").get(key) + " " + value);
////        });
////        System.out.println(citationsMap.get("References").get(144).length());
////        //System.out.println(citedPublications);
////        //System.out.println(publicationsMap);
////        System.out.println(citedPublications);
//
//        System.out.println(publicationsMap.get("Title"));
//
//        System.out.println("!!!!!!!!!!!");
//        citedPublications.forEach((key, value) ->{
//            System.out.println(publicationsMap.get("Title").get(key) + " " + value);
//        });
//
//
//
//    }


}
