import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

@SuppressWarnings("unused")

public class Coin {
	private PrivateKey privateKey = null;
	public PublicKey publicKey = null;
	private byte[] id;
	private double value;
	public static int numCoins;
	private int numCoin;
	
	public Coin(PrivateKey prik, PublicKey pubk) throws NoSuchAlgorithmException{
		privateKey = prik;
		publicKey = pubk;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update((++numCoins+"").getBytes());
		id = md.digest();
		numCoin = numCoins;
		value = numCoins;
	}
	
	public Coin() throws NoSuchAlgorithmException {
		numCoins++;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update((numCoins+"").getBytes());
		id = md.digest();
		numCoin = numCoins;
		value = numCoins;
	}
	
	public void exchangeOwnership(PrivateKey prik, PublicKey pubk) {
		privateKey = prik;
		publicKey = pubk;
	}
	
	private boolean isOwner(byte[] pk) {
		return publicKey.equals(pk);
	}
	
	public void getId() {
		System.out.println("Coin : " + this.id);
		System.out.println("Value = " + numCoin);
	}
	
	public PublicKey getOwner() {
		return publicKey;
	}
	
	public double getValue() {
		return value;
	}
	
	public boolean equals(Coin c2) {
		return this.id == c2.id;
	}
}
