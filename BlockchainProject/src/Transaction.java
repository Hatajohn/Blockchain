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

@SuppressWarnings("unused")

public class Transaction {
	PublicKey sender;
	PublicKey recipient;
	public String id;
	public byte[] signature;
	public float value;
	
	public ArrayList<TransactionIn> ledger = new ArrayList<TransactionIn>();
	public ArrayList<TransactionOut> reciept = new ArrayList<TransactionOut>();
	private static int numTransactions;
	
	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionIn> input) {
		this.ledger = input;
		this.sender = from;
		this.recipient = to;
		this.value = value;
	}
	
	private String calcHash() {
		numTransactions++;
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) + 
				StringUtil.getStringFromKey(recipient) +
				Float.toString(value) + numTransactions
				);
	}
	
	
	public void genSig(PrivateKey prik) throws NoSuchAlgorithmException {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
		signature = StringUtil.applySig(prik, data);
	}
	
	public boolean verifySig() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
		return StringUtil.checkSig(sender, data, signature);
	}
	
	public boolean processTransaction() {
		if(verifySig() == false) {
			System.out.println("Signature failed to verify");
			return false;
		}
		
		for(TransactionIn i : ledger) {
			i.tout = Chain.unTransOut.get(i.toId);
		}
		
		if(getInValue() < Chain.minimumTransaction) {
			System.out.println("#Transaction Inputs too small: " + getInValue());
			return false;
		}
		
		float remainder = getInValue() - value;
		id = calcHash();
		reciept.add(new TransactionOut(this.recipient, value, id));
		reciept.add(new TransactionOut(this.sender, remainder, id));
		
		for(TransactionOut o: reciept) {
			Chain.unTransOut.put(o.id, o);
		}
		
		for(TransactionIn i : ledger) {
			if(i.tout == null) continue;
			Chain.unTransOut.remove(i.tout.id);
		}
		
		return true;
	}
	
	public float getInValue() {
		float total = 0;
		for(TransactionIn i : ledger) {
			if(i.tout == null) continue;
			total += i.tout.value;
		}
		return total;
	}
	
	public float getOutValue() {
		float total = 0;
		for(TransactionOut o : reciept) {
			total+= o.value;
		}
		return total;
	}
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException {	
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		System.out.println("Private and public keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.prik));
		System.out.println(StringUtil.getStringFromKey(walletA.pubk));
		Transaction transaction = new Transaction(walletA.pubk, walletB.pubk, 5, null);
		transaction.genSig(walletA.prik);
		System.out.print("Is signature verified: ");
		System.out.print(transaction.verifySig() + "\n");
	}
}
