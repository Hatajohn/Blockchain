import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;

public class StringUtil {

	

public static String applySha256(String input){		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        

			byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer(); 
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static byte[] applySig(PrivateKey prik, String input) throws NoSuchAlgorithmException {
		 SecureRandom sr = new SecureRandom();
		 KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
		KeyPair kp = kpg.generateKeyPair();
		
		
		Signature sig;
		byte[] output = new byte[0];
		
		try {
			sig = Signature.getInstance("SHA256WithDSA");
			sig.initSign(prik);
			
			output = input.getBytes();
			sig.update(output);
			byte[] digitalSig = sig.sign(); // signed for sig1
			output = digitalSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return output;
	}
	
	public static boolean checkSig(PublicKey pubk, String data, byte[] signature) {
		Signature sig;
		try {
			sig = Signature.getInstance("SHA256WithDSA");
			sig.initVerify(pubk);
			sig.update(data.getBytes());
			return sig.verify(signature);
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getMerkleRoot(ArrayList<Transaction> trans) {
		int count = trans.size();
		ArrayList<String> layer = new ArrayList<String>();
		for(Transaction t : trans) {
			layer.add(t.id);
		}
		ArrayList<String> treeLayer = layer;
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i = 1; i < layer.size(); i++){
				treeLayer.add(applySha256(layer.get(i-1) + layer.get(i)));
			}
			count = treeLayer.size();
			layer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}

}