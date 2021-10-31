import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder result = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i += totalSlices){
            result.append(message.charAt(i));
        }
        return result.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker CC = new CaesarCracker();
        for (int i = 0; i < klength; i++){
            key[i] = CC.getKey(sliceString(encrypted, i, klength));
        }
        return key;
    }

    public void breakVigenere () {
        FileResource fr = new FileResource();
        String message = fr.asString();
        HashMap<String, HashSet<String>> map= new HashMap<String, HashSet<String>>();
        String[] lables = {"Danish", "Dutch", "English", "French", "German", "Italian", "Portuguese", "Spanish"};
        for (String lag : lables){
            FileResource di = new FileResource("dictionaries/" + lag);
            HashSet<String> dictionary = readDictionary(di);
            map.put(lag, dictionary);
            System.out.println("language is :" + lag + " " + dictionary.size());
        }
        
        String decrypted = breakForAllLangs(message, map);
        System.out.println(decrypted);
        //System.out.println(countWords(decrypted, dictionary));
    }
    
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> dictionary = new HashSet<String>();
        for (String line : fr.lines()){
            String word = line.toLowerCase();
            dictionary.add(word);
        }
        return dictionary;
    }
    
    public int countWords(String message, HashSet<String> dictionary){
        int sum = 0;
        String[] words = message.split("\\W+");
        for (int i = 0; i < words.length; i++){
            String word = words[i].toLowerCase();
            if (dictionary.contains(word)){
                sum += 1;
            }
        }
        return sum;
    }
    
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        int maxi = 0;
        String result = "";
        int klength = 0;
        char mostUsed = mostCommonCharIn(dictionary);
        for (int i = 1; i < 101; i++){
            int[] key = tryKeyLength(encrypted, i, mostUsed);
            VigenereCipher VC = new VigenereCipher(key);
            String message = VC.decrypt(encrypted);
            if (countWords(message, dictionary) > maxi){
                maxi = countWords(message, dictionary);
                result = message;
                klength = key.length;
            }
        }
        System.out.println(klength);
        return result;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
        int[] occurencies = new int[27];
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for (String word : dictionary){
            for (int i = 0; i < word.length(); i++){
                int index = alphabet.indexOf(word.charAt(i));
                if (index != -1){
                    occurencies[index] += 1;
                }
            }
        }
        CaesarCracker CC = new CaesarCracker();
        int maxIndex = CC.maxIndex(occurencies);
        return alphabet.charAt(maxIndex);
    }
    
    public String breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> map){
        int maxi = 0;
        String result = "";
        String lagUsed = "";
        for (String lag : map.keySet()){
            String message = breakForLanguage(encrypted, map.get(lag));
            int count = countWords(message, map.get(lag));
            if (count > maxi){
                maxi = count;
                result = message;
                lagUsed = lag;
            }
        }
        System.out.println(lagUsed);
        return result;
    }
    public void test(){
        //System.out.println(sliceString("abcdefghijklm", 1, 4));
        //FileResource fr = new FileResource();
        //String encrypted = fr.asString();
        //FileResource di = new FileResource();
        //HashSet<String> dictionary = readDictionary(di);
        //System.out.println(Arrays.toString(tryKeyLength(message, 4, 'e')));
        //System.out.println(countWords(message, dictionary));
        //int[] key = tryKeyLength(encrypted, 38, 'e');
        //VigenereCipher VC = new VigenereCipher(key);
        //String message = VC.decrypt(encrypted);
        //System.out.println(countWords(message,dictionary));
        breakVigenere();
        //System.out.println(mostCommonCharIn(dictionary));
    }
}
