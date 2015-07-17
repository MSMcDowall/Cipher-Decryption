import java.io.*;
import java.util.*;

public class KPT {
    public static void main(String[] args){
        List<String> encryptedHex = new ArrayList<String>();    //The list of all the encrypted blocks in Hex
        String plainHex = "0x416c";                             //The plain text in Hex
        int plainInt = Hex16.convert(plainHex);                 //The plain text as an integer
        int key = 0;                                            //The key to the cipher
        
        //Read in the encrypted file and add to the encryptedHex ArrayList
        try(
            FileReader enFileReader = new FileReader("Part1E.txt");
            Scanner enFileIn = new Scanner(enFileReader)
            ){
            while (enFileIn.hasNextLine()){
                String hex = enFileIn.nextLine().trim();
                encryptedHex.add(hex);
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }

        //Try decrypting the first block of encrypted hex 
        String firstEncryptedHex = encryptedHex.get(0);
        int encryptedInt = Hex16.convert(firstEncryptedHex);
        int decryptedInt = Coder.decrypt(key, encryptedInt);

        //Keep trying all the possible keys until the decrypted text matches the plain text
        while (decryptedInt != plainInt){
            key++;
            decryptedInt = Coder.decrypt(key, encryptedInt);
        }

        //Display the key and the decrypted message
        System.out.println(String.format("The key is: 0x%04x", key));
        StringBuilder displayStringBuilder = new StringBuilder();
        //Using key decrypt all blocks
        for (String hex: encryptedHex){
            int c = Hex16.convert(hex);
            int p = Coder.decrypt(key, c);
            int c0 = p / 256;
            int c1 = p % 256;
            displayStringBuilder.append((char)c0);
            if (c1 != 0)
                displayStringBuilder.append((char)c1);
        }
        String displayString = displayStringBuilder.toString();
        System.out.println("The message is :"  + displayString);   
    }
}
