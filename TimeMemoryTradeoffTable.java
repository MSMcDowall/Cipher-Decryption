import java.io.*;
import java.util.Random;

public class TMT1 {
	public static void main(String[] args){
		final int N = 5000;							//Number of rows in the table
		final int L = 40;							//Number of keys in each row
		int highestKey = 65535;						//The largest 16 bit integer that can be stored so the highest possible key
		String plainHex = "0x4920";					//The plain text in Hex
		int plainInt = Hex16.convert(plainHex);		//The plain text as an integer

		//Create a random number generator
		Random generator = new Random();
        try(FileWriter table = new FileWriter("TMTTable.txt")){
            for (int i = 0; i < N; i++){
            	//Create a row which starts with a random integer
				int x0 = generator.nextInt(highestKey);
				int[] x = new int[L];
				x[0] = x0;
				//Encrypt each of the elements of the row with the previous value of the row
				for (int j = 1; j < L; j++){
					x[j] = Coder.encrypt(x[j-1], plainInt);
				}
				//Write the two values to a txt file
				table.append(x[L-1] + " " + x[0] + "\n");
			}
        }
        catch (IOException e){
            System.out.println("Failed to write file");
        }	
	}
}