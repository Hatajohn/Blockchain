import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		System.out.println("First coin: ");
		Coin c1 = new Coin();
		c1.getId();
		
		Coin c2 = new Coin();
		Coin c3 = new Coin();
		
		System.out.println("\nWallet test: ");
		Wallet w = new Wallet();
		
		System.out.println("BALANCE = " + w.getBalance());
	}

}
