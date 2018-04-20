package searchTests;

import model.Author;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Raul on 19/04/2018.
 */
public class RandomGenerator {

    public static Random random = new Random();

    public static char generateRandomLetter(){
        return (char) ('a' + random.nextInt(26));
    }


    public static String generateQueryStringFromAuthors(List<String> authors){
        int n = authors.size();
        int randomPosition = random.nextInt(n);
        String queryString = authors.get(randomPosition);


        int size = queryString.length();
        int i = random.nextInt(size - 1);
        int j = i + random.nextInt(size - i - 1);
        return queryString.substring(i, j + 1).toLowerCase();


//        int binaryRandom = random.nextInt(2);
//        if(binaryRandom == 0){
//
//        }
//        else {
//            int surnameSize = author.getSurname().length();
//            int i = random.nextInt(surnameSize - 1);
//            int j =  i + random.nextInt(surnameSize - i - 1);
//            return author.getSurname().substring(i, j + 1);
//
//        }
    }

    public static String generateRandom(int minimumLength, int extraLength) {
        extraLength = random.nextInt(extraLength);
        if(extraLength < 0)
            extraLength = -extraLength;
        int length = minimumLength + extraLength;
        char[] name = new char[length];
        for (int i = 0; i < length; i++) {
            name[i] = generateRandomLetter();
        }
        return new String(name);
    }
}
