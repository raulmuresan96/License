package start;

import model.Author;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Raul on 09/03/2018.
 */
public class RandomGenerator {
    public static Random random = new Random();

    public static char generateRandomStringLetter(){
        return (char) ('a' + random.nextInt(26));
    }

    public static String generateRandomString(int minimumLength, int extraLength) {
        int length = minimumLength + random.nextInt(extraLength);
        char[] user = new char[length];
        for (int i = 0; i < length; i++) {
            user[i] = generateRandomStringLetter();
        }
        return new String(user);
    }

    public static String generateUser() {
        return RandomGenerator.generateRandomString(8, 12);
    }

    public static List<String> generateSearchStrings(int k){
        List<String> searchStrings = new ArrayList<>();
        for(int i = 0; i < k ; i++){
            searchStrings.add(generateRandomString(3, 3));
        }
        return searchStrings;
    }


    public static void populateFiles(int usersCount, int queriesCount){
        List<String> users = new ArrayList<>();
        //Using words from Scrabble List

//        try (Stream<String> lines = Files.lines(Paths.get("/Users/Raul/Desktop/ProgramareParalelaSem6/src/com/company/ScrabbleWords.txt"), Charset.defaultCharset())) {
//            //nrQueries = (int)lines.count();
//            users = lines.map(String::toLowerCase).collect(Collectors.toList());
////            long numOfLines = lines.count();
////            System.out.println(numOfLines);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        try ( PrintWriter writer = new PrintWriter("/Users/Raul/Desktop/ProgramareParalelaSem6/src/com/company/users.txt", "UTF-8");) {
//            for(String user: users){
//                writer.write(user + "\n");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        try ( PrintWriter writer = new PrintWriter("src/main/resources/users.txt", "UTF-8");) {
            for(int i = 0; i < usersCount; i++){
                String firstame = RandomGenerator.generateUser();
                String surname = RandomGenerator.generateUser();
                //System.out.println(Integer.toString(i) + " " + firstame + " " + surname);
                writer.write(Integer.toString(i) + " " + firstame + " " + surname + "\n");
                users.add(firstame);
                users.add(surname);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int currentIndex = usersCount;
        try (PrintWriter writer = new PrintWriter("src/main/resources/query.txt", "UTF-8");) {
            for(int i = 0; i < queriesCount; i++){
                double randomDouble = random.nextDouble();
                if(randomDouble > 0.4){
                    int randomInt = random.nextInt(50);
                    String string = users.get(randomInt);
                   // System.out.println(string.length());
                    int randomLength = random.nextInt(string.length() - 3);
                    writer.write("GET " + users.get(randomInt).substring(randomLength) + "\n");
                    // System.out.println("GET " + searchStrings.get(randomInt));
                    //System.out.println( + " " +  searchEngine.search(searchStrings.get(randomInt)));
                    //searchEngine.search(searchStrings.get(randomInt));
                }
                else if(randomDouble < 0.01){
                    //System.out.println("ADD " + RandomGenerator.generateRandomString(4, 8));
                    writer.write("ADD " + Integer.toString(currentIndex) + " " + RandomGenerator.generateUser() + " " +
                            RandomGenerator.generateUser() + "\n");
                    currentIndex++;
                    //searchEngine.addUser(RandomGenerator.generateRandomString(4, 8));
                }
                else{
                    //searchEngineSerial.searchUser(RandomGenerator.generateRandomString(2, 3));
                    String searchWord = RandomGenerator.generateRandomString(2, 3);
                    //searchEngine.search(searchWord);
                    //System.out.println("GET " + searchWord);
                    writer.write("GET " + searchWord + "\n");
                    //System.out.println(searchWord + " " + searchEngine.search(searchWord));
                }
            }
            System.out.println("Finished generating file");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public static List<Author> populateInputFile(int usernameCount){
        List<Author> authors = new ArrayList<>();
        for(int i = 0; i < usernameCount; i++){
            String firstName = RandomGenerator.generateUser();
            authors.add(new Author());
            //writer.write( + "\n");
        }
        return authors;
    }


    public static void populateQueryFile(int queriesCount){
        List<String> searchStrings = RandomGenerator.generateSearchStrings(50);
        try (PrintWriter writer = new PrintWriter("/Users/Raul/Desktop/ProgramareParalelaSem6/src/com/company/query.txt", "UTF-8");) {
            for(int i = 0; i < queriesCount; i++){
                double randomDouble = random.nextDouble();
                if(randomDouble > 0.4){
                    int randomInt = random.nextInt(searchStrings.size());
                    String string = searchStrings.get(randomInt);
                    int randomLength = random.nextInt(string.length() - 3);
                    writer.write("GET " + searchStrings.get(randomInt).substring(randomLength) + "\n");
                   // System.out.println("GET " + searchStrings.get(randomInt));
                    //System.out.println( + " " +  searchEngine.search(searchStrings.get(randomInt)));
                    //searchEngine.search(searchStrings.get(randomInt));
                }
                else if(randomDouble < 0.1){
                    //System.out.println("ADD " + RandomGenerator.generateRandomString(4, 8));
                    writer.write("ADD " + RandomGenerator.generateRandomString(8, 12) + "\n");
                    //searchEngine.addUser(RandomGenerator.generateRandomString(4, 8));
                }
                else{
                    //searchEngineSerial.searchUser(RandomGenerator.generateRandomString(2, 3));
                    String searchWord = RandomGenerator.generateRandomString(3, 3);
                    //searchEngine.search(searchWord);
                    //System.out.println("GET " + searchWord);
                    writer.write("GET " + searchWord + "\n");
                    //System.out.println(searchWord + " " + searchEngine.search(searchWord));
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter("/Users/Raul/Desktop/ProgramareParalelaSem6/src/com/company/users.txt", "UTF-8");
//            for(int i = 0; i < usernameCount; i++){
//                writer.write(generateRandomString(4, 8) + "\n");
//            }
//            writer.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }

}
