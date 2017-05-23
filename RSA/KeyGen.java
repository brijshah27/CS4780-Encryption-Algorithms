package project_2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyGen {
  public static final boolean is_noisy = true;
  
  private KeyGen() {}
  
  public static void main(String[] args) {
    BigInteger[] keys = getKeys();
    writeKeys(keys);
  }
  
  
  public static BigInteger[] getKeys() {
    return getKeys(1024);
  }
  
  
  public static BigInteger[] getKeys(int numberOfBits) {
    SecureRandom rand = new SecureRandom();
    if (numberOfBits <= 0 || !((numberOfBits & (numberOfBits - 1)) == 0)) {
      throw new IllegalArgumentException();
    }
    int factorLength = numberOfBits / 2;
    BigInteger p = null;
    BigInteger q = null;
    BigInteger n = null;
    boolean state = true;
    while (state) {
      p = new BigInteger(factorLength, Integer.MAX_VALUE, rand);
      q = new BigInteger(factorLength, Integer.MAX_VALUE, rand);
      n = p.multiply(q);
      BigInteger nConstraint = BigInteger.TEN.pow(50 * (int)Math.floor(((0.0f + numberOfBits) / 1024)));
      BigInteger pMinusQ = p.subtract(q).abs();
      state = nConstraint.compareTo(pMinusQ) > 0;
      //System.out.println(nConstraint);
      //System.out.println(pMinusQ);
    }
    BigInteger totientN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // == totient(n)
    BigInteger e;
    do {
      e = new BigInteger(numberOfBits, rand);
    } while ((!totientN.gcd(e).equals(BigInteger.ONE)) || e.equals(BigInteger.ZERO));
    BigInteger d = e.modInverse(totientN);
    if (is_noisy) {
      System.out.printf("p is: %d%nq is: %d%nn is: %d%ne is: %d%nd is: %d%n", p, q, n, e, d);
    }
    return new BigInteger[] { n, e, d };
  }
  
  public static void writeKeys(BigInteger[] keys) {
	  
    File publicKeyFile = new File("pubkey.rsa");
    File privateKeyFile = new File("privkey.rsa");
    ObjectOutputStream publicKeyOut = null;
    ObjectOutputStream privateKeyOut = null;
    try {
    	
      publicKeyOut = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
      privateKeyOut = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
      publicKeyOut.writeObject(keys[0]);
      privateKeyOut.writeObject(keys[0]);
      publicKeyOut.writeObject(keys[1]);
      privateKeyOut.writeObject(keys[2]);
      if(is_noisy) {
    	  
        System.out.println("Keys written !!");
      }
    } catch (IOException e) {
    	
      if(is_noisy) {
        System.out.println("Keys writting failed !!");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (publicKeyOut != null) {
        try {
          publicKeyOut.close();
        } catch (IOException e) {
          
        }
      }
      if (privateKeyOut != null) {
        try {
          
        	privateKeyOut.close();
        } catch (IOException e) {
        
        	
        }
      }
    }
  }
}
