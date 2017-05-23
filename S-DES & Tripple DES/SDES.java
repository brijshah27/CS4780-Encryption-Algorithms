import java.util.Scanner;

public class SDES {
	private static byte[] key = new byte[10];
	private static byte[] k1 = new byte[8];
	private static byte[] k2 = new byte[8];

	public static void main(String[] args) {

		
		String plain_cipher, bit_key, info = null;

		

				System.out.println("ENTER 1 => Encryption");
				System.out.println("ENTER 2 => Decryption");
				Scanner sc = new Scanner(System.in);
				int input = sc.nextInt();
				
				switch(input){

				case 1: {
					System.out.println("\n----------------*Encrypt*---------------\n");

					System.out.print("Enter 10-bit Key for Encryption: ");
					bit_key = sc.next();

					System.out.print("Enter 8-bit Plaintext for Encryption: ");
					plain_cipher = sc.next();

					byte[] encryptedText = Encrypt(convertToByte(bit_key), convertToByte(plain_cipher));
					System.out.print("\nYour Ciphertext is: ");

					for (byte text : encryptedText) {
						System.out.print(text);
					}

				} 
				break;
				case 2:	
				{

					System.out.println("\n----------------*Decrypt*---------------\n");

					System.out.print("Enter 10-bit Key for Decryption: ");
					bit_key = sc.next();

					System.out.print("Enter 8-bit ciphertext for Decryption: ");
					plain_cipher = sc.next();

					byte[] decryptedText = Decrypt(convertToByte(bit_key), convertToByte(plain_cipher));
					System.out.print("\nYour Plaintext is: ");

					for (byte text : decryptedText) {
						System.out.print(text);
					}

				}
				break;
				default:{
				System.out.println("\n-----------------*THANK YOU*----------------------\n");
				sc.close();
				}
				break;
				}
	}
			
		

	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {

		key = rawkey;
		generateKey();

		ciphertext = InitialPermutationOperation(ciphertext);

		byte[] LH = new byte[4];
		byte[] RH = new byte[4];
		LH[0] = ciphertext[0];
		LH[1] = ciphertext[1];
		LH[2] = ciphertext[2];
		LH[3] = ciphertext[3];

		RH[0] = ciphertext[4];
		RH[1] = ciphertext[5];
		RH[2] = ciphertext[6];
		RH[3] = ciphertext[7];

		int[] r2 = new int[8];
		r2 = functionFk(LH, RH, k2);

		byte[] temp = new byte[8];
		temp = switchSW(r2);

		LH[0] = temp[0];
		LH[1] = temp[1];
		LH[2] = temp[2];
		LH[3] = temp[3];

		RH[0] = temp[4];
		RH[1] = temp[5];
		RH[2] = temp[6];
		RH[3] = temp[7];

		int[] r1 = new int[8];
		r1 = functionFk(LH, RH, k1);

		byte[] k = new byte[r1.length];
		for (int i = 0; i < r1.length; i++) {
			k[i] = Byte.parseByte(Integer.toString(r1[i]));
		}

		ciphertext = k;
		ciphertext = invPerm(ciphertext);

		return ciphertext;
	}

	public static byte[] InitialPermutationOperation(byte[] plaintext) {
		byte[] temp = new byte[8];
		int[] order = { 1, 5, 2, 0, 3, 7, 4, 6 };

		for (int i = 0; i < 8; i++) {
			
			temp[i] = plaintext[order[i]];
		}
		return temp;
	}

	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		key = rawkey;
		generateKey();

		plaintext = InitialPermutationOperation(plaintext);

		byte[] LH = new byte[4];
		byte[] RH = new byte[4];
		LH[0] = plaintext[0];
		LH[1] = plaintext[1];
		LH[2] = plaintext[2];
		LH[3] = plaintext[3];

		RH[0] = plaintext[4];
		RH[1] = plaintext[5];
		RH[2] = plaintext[6];
		RH[3] = plaintext[7];

		int[] r1 = new int[8];
		r1 = functionFk(LH, RH, k1);

		byte[] temp = new byte[8];
		temp = switchSW(r1);

		LH[0] = temp[0];
		LH[1] = temp[1];
		LH[2] = temp[2];
		LH[3] = temp[3];

		RH[0] = temp[4];
		RH[1] = temp[5];
		RH[2] = temp[6];
		RH[3] = temp[7];

		int[] r2 = new int[8];
		r2 = functionFk(LH, RH, k2);

		byte[] k = new byte[r2.length];
		for (int i = 0; i < r2.length; i++) {
			k[i] = Byte.parseByte(Integer.toString(r2[i]));
		}

		plaintext = k;
		plaintext = invPerm(plaintext);

		return plaintext;
	}

	

	public static byte[] switchSW(int[] in) {

		int[] temp = new int[8];
		
		int[] order = { 4, 5, 6, 7, 0, 1, 2, 3 };
		for (int i = 0; i < 8; i++) {
			
			temp[i] = in[order[i]];
		}

		byte[] kl = new byte[temp.length];
		for (int i = 0; i < temp.length; i++) {
			
			kl[i] = Byte.parseByte(Integer.toString(temp[i]));
		}

		return kl;
	}
	public static byte[] invPerm(byte[] plaintext) {

		byte[] temp = new byte[8];
		int[] order = { 3, 0, 2, 4, 6, 1, 7, 5 };
		for (int i = 0; i < 8; i++) {
			temp[i] = plaintext[order[i]];
		}

		return temp;

	}
	public static int[] functionFk(byte[] L, byte[] R, byte[] SK) {

		int[] temp = new int[4];
		int[] out = new int[8];

		temp = mappingF(R, SK);

		int[] k = new int[L.length];
		for (int i = 0; i < L.length; i++) {
			k[i] = Integer.parseInt(Byte.toString(L[i]));
		}

		int[] g = new int[R.length];
		for (int i = 0; i < R.length; i++) {
			g[i] = Integer.parseInt(Byte.toString(R[i]));
		}

		out[0] = k[0] ^ temp[0];
		out[1] = k[1] ^ temp[1];
		out[2] = k[2] ^ temp[2];
		out[3] = k[3] ^ temp[3];

		out[4] = g[0];
		out[5] = g[1];
		out[6] = g[2];
		out[7] = g[3];

		return out;

	}
	public static void permutationP10() {
		byte[] temp = new byte[10];
		int[] order = { 2, 4, 1, 6, 3, 9, 0, 8, 7, 5 };

		for (int i = 0; i < 10; i++) {
			temp[i] = key[order[i]];
		}
		key = temp;
	}


	public static void generateKey() {

		permutationP10();
		leftshiftLS1();
		k1 = permutationP8();
		leftshiftLS2();
		k2 = permutationP8();

	}


	public static byte[] convertToByte(String givenKey) {

		char c1;
		String ts;
		byte[] bytes = new byte[givenKey.length()];

		try {
			for (int i = 0; i < givenKey.length(); i++) {
				c1 = givenKey.charAt(i);
				ts = Character.toString(c1);
				bytes[i] = Byte.parseByte(ts);

				if (bytes[i] != 0 && bytes[i] != 1) {
					throw new RuntimeException("\n Not a valid key ");
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return bytes;
	}

	
	

	public static void leftshiftLS1() {
		byte[] temp = new byte[10];
		int[] order = { 1, 2, 3, 4, 0, 6, 7, 8, 9, 5 };
		for (int i = 0; i < 10; i++) {
			temp[i] = key[order[i]];
		}
		key = temp;
	}

	public static byte[] permutationP8() {
		byte[] temp = new byte[8];
		int[] order = { 5, 2, 6, 3, 7, 4, 9, 8 };
		for (int i = 0; i < 8; i++) {
			temp[i] = key[order[i]];
		}
		return temp;

	}

	public static void leftshiftLS2() {
		byte[] temp = new byte[10];
		int[] order = { 2, 3, 4, 0, 1, 7, 8, 9, 5, 6 };

		for (int i = 0; i < 10; i++) {
			temp[i] = key[order[i]];
		}
		key = temp;
	}
	public static int[] mappingF(byte[] R, byte[] SK) {
		int[] temp = new int[8];
		int[] order = { 3, 0, 1, 2, 1, 2, 3, 0 };

		int[] k = new int[SK.length];
		for (int i = 0; i < SK.length; i++) {
			k[i] = Integer.parseInt(Byte.toString(SK[i]));
		}

		int[] gf = new int[R.length];
		for (int i = 0; i < R.length; i++) {
			gf[i] = Integer.parseInt(Byte.toString(R[i]));
		}

		for (int i = 0; i < temp.length; i++) {
			temp[i] = gf[order[i]];
		}

		temp[0] = temp[0] ^ k[0];
		temp[1] = temp[1] ^ k[1];
		temp[2] = temp[2] ^ k[2];
		temp[3] = temp[3] ^ k[3];
		temp[4] = temp[4] ^ k[4];
		temp[5] = temp[5] ^ k[5];
		temp[6] = temp[6] ^ k[6];
		temp[7] = temp[7] ^ k[7];

		final int[][] S0 = { { 1, 0, 3, 2 }, { 3, 2, 1, 0 }, { 0, 2, 1, 3 }, { 3, 1, 3, 2 } };
		final int[][] S1 = { { 0, 1, 2, 3 }, { 2, 0, 1, 3 }, { 3, 0, 1, 0 }, { 2, 1, 0, 3 } };

		int d11 = temp[0];
		int d14 = temp[3];

		int row1 = BinaryOp.BinToDec(d11, d14);

		int d12 = temp[1];
		int d13 = temp[2];
		int col1 = BinaryOp.BinToDec(d12, d13);

		int o1 = S0[row1][col1];

		int[] out1 = BinaryOp.DecToBinArr(o1);

		int d21 = temp[4];
		int d24 = temp[7];
		int row2 = BinaryOp.BinToDec(d21, d24);

		int d22 = temp[5];
		int d23 = temp[6];
		int col2 = BinaryOp.BinToDec(d22, d23);

		int o2 = S1[row2][col2];

		int[] out2 = BinaryOp.DecToBinArr(o2);

		int[] out = new int[4];
		out[0] = out1[0];
		out[1] = out1[1];
		out[2] = out2[0];
		out[3] = out2[1];

		int[] O_Per = new int[4];
		O_Per[0] = out[1];
		O_Per[1] = out[3];
		O_Per[2] = out[2];
		O_Per[3] = out[0];

		return O_Per;
	}
}
class BinaryOp {

	public static int BinToDec(int... bits) {

		int initial = 0;
		int base = 1;
		for (int i = bits.length - 1; i >= 0; i--) {
			initial = initial + (bits[i] * base);
			base = base * 2;
		}

		return initial;
	}

	public static int[] DecToBinArr(int no) {

		if (no == 0) {
			int[] zero = new int[2];
			zero[0] = 0;
			zero[1] = 0;
			return zero;
		}
		int[] initial = new int[10];

		int count = 0;
		for (int i = 0; no != 0; i++) {
			initial[i] = no % 2;
			no = no / 2;
			count++;
		}

		int[] temp = new int[count];

		for (int i = count - 1, j = 0; i >= 0 && j < count; i--, j++) {
			temp[j] = initial[i];
		}

		if (count < 2) {
			initial = new int[2];
			initial[0] = 0;
			initial[1] = temp[0];
			return initial;
		}

		return temp;
	}
}


