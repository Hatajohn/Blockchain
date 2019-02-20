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
import java.util.HashMap;
import java.util.Map.Entry;

public class Wallet {
	public PrivateKey prik;
	public PublicKey pubk;
	public double balance = 0.0;
	private static int numWallets;
	
	public HashMap<String,TransactionOut> unTransOuts = new HashMap<String,TransactionOut>(); 

	
	private SecureRandom secureRandom = new SecureRandom();
	private KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
	private KeyPair kp = kpg.generateKeyPair();
	
	public Wallet() throws NoSuchAlgorithmException {
		prik = kp.getPrivate();
		pubk = kp.getPublic();
	}
	
	public Wallet getWallet(){
		return this;
	}
	
	public PublicKey getPublic() {
		return pubk;
	}


	public float getBalance() {
		float total = 0;	
        for (Entry<String, TransactionOut> item: Chain.unTransOut.entrySet()){
        	TransactionOut unTransOut = item.getValue();
            if(unTransOut.isMine(pubk)) { //checks if coin belongs to me
            	unTransOuts.put(unTransOut.id,unTransOut); //add it to our list of unspent transactions.
            	total += unTransOut.value ; 
            }
        }  
		return total;
	}
	//Create and return a new transaction 
	public Transaction sendFunds(PublicKey _recipient,float value ) throws NoSuchAlgorithmException {
		if(getBalance() < value) { //Verify if there is enough funds for it to work
			System.out.println("I am sorry there were not enough funds to complete this. Transaction has been discareded");
			return null;
		}
    //Here we are creating an arraylist of outputs 
		ArrayList<TransactionIn> inputs = new ArrayList<TransactionIn>();
    
		float total = 0;
		for (Entry<String, TransactionOut> item: unTransOuts.entrySet()){
			TransactionOut unTransOut = item.getValue();
			total += unTransOut.value;
			inputs.add(new TransactionIn(unTransOut.id));
			if(total > value) break;
		}
		
		Transaction newTransaction = new Transaction(pubk, _recipient , value, inputs);
		newTransaction.genSig(prik);
		
		for(TransactionIn input: inputs){
			unTransOuts.remove(input.toId);
		}
		return newTransaction;
	}
	

}
