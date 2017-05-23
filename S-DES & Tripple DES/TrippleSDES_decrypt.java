
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TrippleSDES_decrypt {
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
	 
	 public static int getBit(String s, int index){
        if(index < 0 || index >= s.length())
            return 0;
        
        if(s.charAt(index) == '0')
            return 0;
        else
            return 1;
        
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
	 
	public static void main(String[] args) throws FileNotFoundException {
		//read file location.
		Scanner msg1 = new Scanner(new File("/Users/bug/Downloads/SDES 3/SDES/src/msg1.txt"));
		
		String msg = "";
		
		//read trough end of file
		while(msg1.hasNext()){
			msg = msg1.next();
		}
		msg1.close();
		
		//Convert content of file to Byte Array.
		byte[] msgBytes = CovertToByteArr(msg);
		System.out.println(msg);
		printArr(msgBytes);
		System.out.println("\n" + CASCII.toString(msgBytes));
		
		String keyString1 = "0000000000";
		String keyString2 = "0000000000";
		byte[] key1 = new byte[10];
		byte[] key2 = new byte[10];
		byte[] decrypted = new byte[8];
		byte[] temp = new byte[8];
		
		//Generate all possible keys and apply TrippleSDES to decrypt text.
		for (int j = 0; j < 1023; j++) {
			msgBytes = CovertToByteArr(msg);
			keyString1 = addBinary(keyString1, "0000000001");
			key1 = CovertToByteArr(keyString1);
			keyString2 = "0000000000";
			
			for (int k = 0; k < 2047; k++) {
				keyString2 = addBinary(keyString2, "0000000001");
				key2 = CovertToByteArr(keyString2);
				
				for (int i = 0; i < msgBytes.length; i++) {
					if (i % 8 == 7) {
						System.arraycopy(msgBytes, i - 7, temp, 0, temp.length);
						decrypted = TripleSDES.Decrypt(key1, key2, temp);
						System.arraycopy(decrypted, 0, msgBytes, i - 7, decrypted.length);
					}
				}
				
				
				
				
				
				System.out.print("Key 1:");
				printArr(key1);
				System.out.print("\nKey 2:");
				printArr(key2);
				System.out.println("\nPlain text:" + CASCII.toString(msgBytes) + "\n");
			}
		}
	}
}