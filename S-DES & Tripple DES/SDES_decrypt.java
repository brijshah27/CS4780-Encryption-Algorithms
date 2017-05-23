
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SDES_decrypt {

	
	 public static String addBinary(String a, String b) {
        
        int left = a.length();
        int right = b.length();
        
        int max = Math.max(left, right);
        
        StringBuilder sum = new StringBuilder("");
        int carry = 0;
        
        for(int i = 0; i < max; i++){
            int m = getBit(a, left - i - 1);
            int n = getBit(b, right - i - 1);
            int add = m + n + carry;
            sum.append(add % 2);
            carry = add / 2;
        }
        
        if(carry == 1)
            sum.append("1");
        
        return sum.reverse().toString();
        
    }
	
	 public static byte[] CovertToByteArr(String bytes){
			byte[] b = bytes.getBytes();
			for (int i = 0; i < b.length; i++){
				b[i] = (byte) (b[i] - 48);
			}
			 
			 return b;
		 }
	 
	 public static void printArr(byte[] bytes) {
		 for (int i = 0; i < bytes.length; i++){
		 System.out.print(bytes[i]);
		 }
		 }
	 
	 public static int getBit(String s, int index){
        if(index < 0 || index >= s.length())
            return 0;
        
        if(s.charAt(index) == '0')
            return 0;
        else
            return 1;
        
	 }
	
	 
	public static void main(String[] args) throws FileNotFoundException {
		//read file
		Scanner msg1 = new Scanner(new File("/Users/bug/Downloads/SDES 3/SDES/src/msg1.txt"));
		
		String msg = "";
		
		//read trough end of text file
		while(msg1.hasNext()){
			msg = msg1.next();
		}
		msg1.close();
		
		//convert file text to byte array
		byte[] msgBytes = CovertToByteArr(msg);
		System.out.println(msg);
		printArr(msgBytes);
		System.out.println("\n" + CASCII.toString(msgBytes));
		
		//Generate keys and do decryption.
		String keyString = "0000000000";
		byte[] key = new byte[10];
		byte[] decrypted = new byte[8];
		byte[] temp = new byte[8];
		
		for (int j = 0; j < 2047; j++) {
			msgBytes = CovertToByteArr(msg);
			keyString = addBinary(keyString, "0000000001");
			key = CovertToByteArr(keyString);
			System.out.print("Generated Key:");
			printArr(key);
			
			//Apply all key
			for (int i = 0; i < msgBytes.length; i++) {
				if (i % 8 == 7){
					System.arraycopy(msgBytes, i - 7, temp, 0, temp.length);
					decrypted = SDES.Decrypt(key, temp);
					System.arraycopy(decrypted, 0, msgBytes, i - 7, decrypted.length);
				}
			}
			
			System.out.println("\nPlain Text: " + CASCII.toString(msgBytes) + "\n");
		}
	}
}
