package project_2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

public class ChangeByte {
	private ChangeByte() {}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("*******************Enter name of signed file to tamper with: **************************\n");
		String fileName = sc.next();
		
		
		File tampFile = new File(fileName);
		byte[] tamperBinary = byteFetcher(tampFile);
		
		System.out.print("Total bytes (0-" + (tamperBinary.length-1) + ") to tamper: ");
		String dumpInput = sc.next();
		while(!dumpInput.matches("\\d+") || (Integer.parseInt(dumpInput) < 0 || Integer.parseInt(dumpInput) >= tamperBinary.length)){
			System.out.print("Enter an number between 0 and " + (tamperBinary.length-1) + "to temper bytes: ");
			dumpInput = sc.next();
		}
		
		int bytePosition = Integer.parseInt(dumpInput);
		
		tamperFile(tampFile,bytePosition,tamperBinary);
		
		sc.close();
		System.exit(0);
	}

	public static byte[] byteFetcher(File file) {
		if(!file.exists()){
			System.out.println("Can't read bytes from nonexist file!!");
			return null;
		}
		
		BufferedInputStream fileStream = null;
		
		
		byte[] binaryFile = new byte[(int)file.length()];
		
		try{
			fileStream = new BufferedInputStream(new FileInputStream(file));
			fileStream.read(binaryFile); 
			fileStream.close();
			
		} catch (IOException e) {
			System.out.println("Error in file");
		}
		return binaryFile;
	}
	
	public static void tamperFile(File tamfile, int bytePos, byte[] binarray){
		byte[] randbyte = new byte[1];
		new Random().nextBytes(randbyte);
		
		while(randbyte[0] == binarray[bytePos])
			new Random().nextBytes(randbyte);
		
		System.out.println("\nChanging selected byte:");
		
		
		final int SIDERANGE = 2; 
		int min = 0;
		if(binarray.length > SIDERANGE * 2 + 1){
			min = bytePos - (SIDERANGE);
			while(min < 0) min++;
			while(min + (SIDERANGE * 2) > binarray.length) min--;
		}
		String blank = "        ";
		String indic = "________";
		
		for(int a = min; a < min + SIDERANGE * 2 + 1 &&  a < binarray.length; a++)
			if(a != bytePos) System.out.print(blank + " "); else { System.out.print(bytePos); break; }
		System.out.println();
		for(int i = min; i < min + SIDERANGE * 2 + 1 &&  i < binarray.length; i++)
			System.out.print(Integer.toBinaryString((binarray[i] & 0xFF) + 0x100).substring(1) + " ");
		System.out.println();
		for(int j = min; j < min + SIDERANGE * 2 + 1 &&  j < binarray.length; j++)
			if(j == bytePos) System.out.print(indic + " "); else System.out.print(blank + " ");
		System.out.println();
		
		binarray[bytePos] = randbyte[0];
		
		for(int k = min; k < min + SIDERANGE * 2 + 1 &&  k < binarray.length; k++)
			System.out.print(Integer.toBinaryString((binarray[k] & 0xFF) + 0x100).substring(1) + " ");
		System.out.println();
		try {
			OutputStream fileOut = null;
			try {
				fileOut = new BufferedOutputStream(new FileOutputStream(tamfile));
				fileOut.write(binarray);
			} finally {
				fileOut.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("EXCEPTION " + e);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
		
		System.out.println("\n******************The file done tempring with byte index " + bytePos + ".*************************\n");
	}
}
