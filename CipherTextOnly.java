import java.io.*;
import java.util.*;

public class CTO{
	public static void main(String[] args){
		int noOfBlocks = 11;                                      //The number of blocks of cipher text which will be decrypted to find the key
        int highestKey = 65535;                                   //The largest 16 bit integer that can be stored so the highest possible key                          
		Set<String> dictionary = new HashSet<String>();           //The set of all the words in the dictionary
        List<String> encryptedHex = new ArrayList<String>();      //The list of all the encrypted blocks in Hex
        int key = 0;                                              //The key to the cipher

        //Read in the dictionary and store in a set
        //Read in the encrypted file and add to the encryptedHex ArrayList
		try(
            FileReader dictReader = new FileReader("dictionary.txt");
            FileReader enFileReader = new FileReader("Part2E.txt");
            Scanner dictIn = new Scanner(dictReader);
            Scanner enFileIn = new Scanner(enFileReader)
            ){            
            while (dictIn.hasNextLine()){
                String dict = dictIn.nextLine().trim();
                dictionary.add(dict);
            }
            while (enFileIn.hasNextLine()){
                String hex = enFileIn.nextLine().trim();
                encryptedHex.add(hex);
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }

        //The string builder which will be compared with the dictionary
        StringBuilder decryptedStringBuilder = new StringBuilder();

        // Loop through all key possibilities
        for (key = 0; key < highestKey; key++){
            System.out.println(""+ key);
            //Loop through the chosen number of blocks which will be decrypted and compared with the dictionary
            for (int i = 0; i < noOfBlocks; i++){
                //Change the encrypted Hex into an int which can then be decrypted with the current key
                int encryptedInt = Hex16.convert(encryptedHex.get(i));
                int decryptedInt = Coder.decrypt(key, encryptedInt);

                //Turn into text String 
                int c0 = decryptedInt / 256;
                int c1 = decryptedInt % 256;
                decryptedStringBuilder.append((char)c0);
                if (c1 != 0)
                    decryptedStringBuilder.append((char)c1);
            }

            //Create a string of all the decrypted text
            String decryptedString = decryptedStringBuilder.toString();
            String[] decryptedWords = decryptedString.toUpperCase().split(" +");
            int isEnglish = 0;

            //Check array of words against the dictionary
            for (String word: decryptedWords){
                if (dictionary.contains(word))
                    isEnglish++;
            }
            //If enough English words have been found then the correct key
            if (isEnglish > 3){
                break;
            }
        }

        //Display the key and the decrypted message
        StringBuilder displayStringBuilder = new StringBuilder();
        System.out.println(String.format("The key is: 0x%04x", key));
        //Using key decrypt all blocks
        for (String hex: encryptedHex){
            int c = Hex16.convert(hex);
            int p = Coder.decrypt(key, c);
            int c0 = p /256;
            int c1 = p % 256;
            displayStringBuilder.append((char)c0);
            if (c1 != 0)
                displayStringBuilder.append((char)c1);
        }
        String displayString = displayStringBuilder.toString();
        System.out.println("The message is : "  + displayString);
    }
}


