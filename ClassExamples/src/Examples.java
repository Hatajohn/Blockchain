import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

public class Examples {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
		KeyPair kp = kpg.generateKeyPair();
		
		Signature sig = Signature.getInstance("SHA256WithDSA");
		sig.initSign(kp.getPrivate());
		
		byte[] data = "This is a test to be signed".getBytes();
		sig.update(data);
		byte[] digitalSig = sig.sign(); // signed for sig1
		
		PublicKey pk = kp.getPublic();
		Signature sig2 = sig;	// same signature
		
		sig2.initVerify(pk);	
		
		sig2.update(data);
		
		if(sig2.verify(digitalSig)) {	// will verify
			System.out.println("Initial Verified");
		} else {
			System.out.println("Initial Unverified");
		}
		
		PublicKey newKey = kp.getPublic();
		sig2.initVerify(newKey);
		
		if(sig2.verify(digitalSig)) {	// new key shouldn't verify
			System.out.println("New Key Verified");
		} else {
			System.out.println("New Key Unverified");
		}
		
		byte[] data2 = "This is a test to be signed!".getBytes(); // slightly different
		
		sig2.initVerify(pk);
		sig2.update(data2);
		
		if(sig2.verify(digitalSig))	// will not verify
			System.out.println("New Data Verified");
		else
			System.out.println("New Data Unverified");
		
		sig2.update(data);	//	restore data
		
		if(sig2.verify(digitalSig))	// will verify
			System.out.println("Restore Verified");
		else
			System.out.println("Restore Unverified");
	}
}
