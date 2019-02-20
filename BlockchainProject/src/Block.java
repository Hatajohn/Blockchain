//Group 4
import java.util.ArrayList;
import java.util.Date;


public class Block {
	public String hash;
	public String previousHash;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 
	String data; //just a string for now
	private long timeStamp;
	private int nonce;


	public Block(String previousHash ) {
		
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}    

	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce)+
				merkleRoot
				);
		return calculatedhash;
	}
	public String toString() {
		return("Hash: "+ this.hash + "\n")+
				("Previous Hash: "+ this.previousHash + "\n")+
				("Hash: "+ this.hash + "\n");
	}
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = new String(new char[difficulty]).replace('\0', '0'); 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("BLOCK MINED : " + hash);
	}

	public boolean addTransaction(Transaction trans) {
		
		if(trans == null) return false;		
		if((previousHash != "0")) {
			if((trans.processTransaction() != true)) {
				System.out.println("Sorry, it failed to proccess");
				return false;
			}
		}
		transactions.add(trans);
		System.out.println("Congrats, it was added to the block");
		return true;
	}
	

}

