public class SDES_CASCII {

	public static void main(String[] args) {
		byte[] key= CovertToByteArr("0111001101");
		String cryptography = "CRYPTOGRAPHY";
		
		
		byte[] encrypted = new byte[8];
		
		byte[] plain = CASCII.Convert(cryptography);
		byte[] temp = new byte[8];
		
		
		/*
		 * Divide plaintext into 8 bits
		 * Apply SDES
		 * Print the result
		 */
		int i = 0;
		while ( i < plain.length) {
			if (i % 8 == 7){
				System.arraycopy(plain, i - 7, temp, 0, temp.length);
				encrypted = SDES.Encrypt(key, temp);
				System.arraycopy(encrypted, 0, plain, i - 7, encrypted.length);
			}
			i++;
		}
		System.out.println("Plain text:");
		printByteArray(plain);
		System.out.println("\nLength of PlainText : " + plain.length);
		
		
		
		
	}
	
	public static void printByteArray(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++){
			System.out.print(bytes[i]);
		}
}
	public static byte[] CovertToByteArr(String bytes){
		byte[] b = bytes.getBytes();
		for (int i = 0; i < b.length; i++){
		b[i] = (byte) (b[i] - 48);
		}
		 
		 return b;
		 }
}