package project_2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
  
  private Main() {}
  
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (true) {
      System.out.printf("Actions:%n  0   Exit%n  1  **Send** files%n  2  **Receive** files%n");
      int action = 0;
      String actionCapture = sc.next();
      while(!actionCapture.matches("\\d+")) {
    	  System.out.println("Enter a number only please:");
    	  actionCapture = sc.next();
      } 
      action = Integer.parseInt(actionCapture);
      if (action == 1) {
        System.out.println("*********************Enter a filename:***********************\n");
        String filename = sc.next();
        DigitalSignature.writeSigned(new File(filename), getKeys(true));
      } else if (action == 2) {
        System.out.println("**Error: Filename does not ends with \".signed\":**");
        String filename = sc.next();
        if (!filename.matches(".*" + Pattern.quote(".") + "signed")) {
          System.out.println("That file is not valid (not a .signed)");
          continue;
        }
        boolean valid = DigitalSignature.verify(new File(filename), getKeys(false));
        System.out.printf("*******The file has %sbeen tampered with.%n", valid ? "not " : "");
      } else {
        break;
      }
    }
    
    sc.close();
    System.exit(0);
  }
  
  public static BigInteger[] getKeys(boolean privateKey) {
    File keyFile = new File(privateKey ? "privkey.rsa" : "pubkey.rsa");
    ObjectInputStream keyIn = null;
    
    BigInteger n = null;
    BigInteger exponent = null;
    try {
      keyIn = new ObjectInputStream(new FileInputStream(keyFile));
      n = (BigInteger) keyIn.readObject();
      exponent = (BigInteger) keyIn.readObject(); 
      if (KeyGen.is_noisy) {
        System.out.println("Read keys");
      }
    } catch(IOException e) {
      if(KeyGen.is_noisy) System.out.println("*******************");
    } catch(ClassCastException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      if(keyIn != null) {
        try {
          keyIn.close();
        } catch(IOException e) {}
      }
    }
    BigInteger[] keys = { n, exponent };
    return keys;
  }
}
