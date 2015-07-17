import java.io.*;
import java.util.*;

public class TMT2{
	public static void main(String[] args){
        final int L = 40;                                       //Number of keys in each row of the table
        String plainHex = "0x4920";                             //The plain text in Hex
        int plainInt = Hex16.convert(plainHex);                 //The plain text as an integer
		List<String> encryptedHex = new ArrayList<String>();    //The list of all the encrypted blocks in Hex
        Table lookupTable = new Table();                        //The table of randomly generated keys and decryptions
        boolean found = false;                                  //Flag for if the first encrypted block has been found in the table yet

        //Read in the encrypted file and add to the encryptedHex ArrayList
        //Read in the table and create a table object
        try(
            FileReader enFileReader = new FileReader("Part3E.txt");
            Scanner enFileIn = new Scanner(enFileReader);
            FileReader tableReader = new FileReader("TMTTable.txt");
            Scanner tableIn = new Scanner(tableReader);
            ){
            while (enFileIn.hasNextLine()){
                String hex = enFileIn.nextLine().trim();
                encryptedHex.add(hex);
            }
            while (tableIn.hasNextInt()){
                int key = tableIn.nextInt();
                int data = tableIn.nextInt();
                lookupTable.add(key, data);
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }

        //Check if the first encrypted block is at the end of any of the rows
        int encryptedInt = Hex16.convert(encryptedHex.get(0));
        int data = lookupTable.find(encryptedInt);
        if (data != -1){
            found = true;
        }

        //If the block is not at the end of any of the rows see if it is in the middle
        else{
            int[] checkedValues = new int[L];
            checkedValues[0] = encryptedInt;
            for (int i = 1; i < L; i++){
                checkedValues[i] = Coder.encrypt(checkedValues[i-1], plainInt);
                data = lookupTable.find(checkedValues[i]);

                /*After encrypting the original block i times, the value is equal 
                to the end of a row, this means that the block and its key are in that row*/ 
                if (data != -1){
                    found = true;
                    break;

                }
            }
        }

        if (!found){
            System.out.println("Not found in table. Create new table.");
        }
        else{
            int key = 0;
            int[] correctRow = new int[L];
            correctRow[0] = data;

            //Find the key for the block within the row that was found previously
            for (int i =1; i < L; i++){
                correctRow[i] = Coder.encrypt(correctRow[i-1], plainInt);
                if (correctRow[i]==encryptedInt){
                    key = correctRow[i-1];
                    break;
                }
            }
            
            //Display the key and the decrypted message
            System.out.println(String.format("The key is: 0x%04x", key));
            StringBuilder displayStringBuilder = new StringBuilder();
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
            System.out.println("The message is :"  + displayString);
        }
    }
}