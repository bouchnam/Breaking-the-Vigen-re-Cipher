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
        int[] key = tryKeyLength(message, 5, 'e');
        VigenereCipher VC = new VigenereCipher(key);
        String messagedecrypted  = VC.decrypt(message);
        System.out.println(messagedecrypted);
    }
    
    public void test(){
        //System.out.println(sliceString("abcdefghijklm", 1, 4));
        //FileResource fr = new FileResource();
        //String message = fr.asString();
        //System.out.println(Arrays.toString(tryKeyLength(message, 5, 'e')));
        breakVigenere();
    }
}
