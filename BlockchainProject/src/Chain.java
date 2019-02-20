//Group 4
//	https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
// Website was used to get down the nuts and bolts of the blockchain
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Chain {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	static int difficulty = 3;
	public static float minimumTransaction = 0.1f;
	public static HashMap<String, TransactionOut> unTransOut = new HashMap<String, TransactionOut>();
	private static HashMap<String, Wallet> chain = new HashMap<String, Wallet>();

	public static Wallet ragnarok;
	public static Wallet gen;
	public static Transaction genesis;
	
	public static boolean testPointer;
	public static int counter = 0;
	public static void main(String[]args) throws NoSuchAlgorithmException {
		
		boolean running = true;
		ragnarok = new Wallet();	//	requires two wallets to form the genesis transaction
		gen = new Wallet();
		chain.put("0", gen);
	
		genesis = new Transaction(ragnarok.pubk, gen.pubk, 1000f, null);
		genesis.genSig(ragnarok.prik);	 //manually sign the genesis transaction	
		genesis.id = "0"; //manually set the transaction id
		genesis.reciept.add(new TransactionOut(genesis.recipient, genesis.value, genesis.id)); 
		unTransOut.put(genesis.reciept.get(0).id, genesis.reciept.get(0));
//		System.out.println(unTransOut.get(genesis.reciept.get(0).id));
//		System.out.println(genesis.reciept.get(0).parentTransactionId);
//		System.out.println(genesis.sender.equals(ragnarok.pubk));
//		System.out.println(genesis.recipient.equals(gen.pubk));
		System.out.println("Creating and Mining Genesis block... ");
		Block genesisBlock = new Block("0");
		genesisBlock.addTransaction(genesis);
		addBlock(genesisBlock);
		counter = 1;	
		
			Scanner input = new Scanner(System.in);
			System.out.println("Hello welcome to the cryptocurrency simulator");
			System.out.println("Genesis block has already been generated");
			System.out.println("Here is a list of commands");

			System.out.println("CreateWallet");
			System.out.println("RemoveWallet");
			System.out.println("WalletList");
			System.out.println("Send");
			System.out.println("CheckBalance");
			System.out.println("End");
			System.out.println("and creates a temporary wallet, and gives 1000 coins to an initial wallet, called gen");

			while (isChainValid() && running)
				
			{
				String command = input.next();
				switch (command) {
				case "CreateWallet":
					System.out.println("What is the wallet name?");
					chain.put(input.next(), new Wallet());
					System.out.println("Wallet Created");
				    break;
				case "RemoveWallet":	
					System.out.println("What wallet do you want to remove?");
					String remove = input.next();
				    Wallet ticker;
				    ticker = chain.remove(remove);
				    System.out.println("WalletRemoved: " + remove);
				    break;
				case "WalletList":
					for (String str : chain.keySet()) {
						System.out.println(str);	
					}
				  break;
				case "Chain":
					printChain();
					break;
				case "Send":
					System.out.println("Which wallet is the money coming from");
					String send = input.next();
					System.out.println("Which wallet is the money going?");
					String recieve = input.next();
					if(chain.get(send) != null && chain.get(recieve) != null){
						System.out.println("How much money?");
						float amount = input.nextFloat();
						Block temp = new Block(blockchain.get(blockchain.size()-1).hash);
						temp.addTransaction(chain.get(send).sendFunds(chain.get(recieve).getPublic(), amount));
						addBlock(temp);
						System.out.println("Transaction complete");
						System.out.println("Balance of the sending wallet " + chain.get(send).getBalance() );
						System.out.println("Balance of the recieving wallet "+ chain.get(recieve).getBalance());
						break;
					}
					System.out.println("One participant of the transaction does not exist!");
					break;
				case "CheckBalance":
					System.out.println("What wallet do you want to check the balance of?");
					String check = input.next();
					if(!chain.containsKey(check)){
						System.out.println("Wallet does not exist!");
						break;
					}
					System.out.println("The balance of "+ check + ": "+ chain.get(check).getBalance());
					break;
				case "End":
					System.out.println("Thank you for using our blockchain!\nYour session has now ended.");
					running = false;
				}
			}
		}


	public Chain() throws NoSuchAlgorithmException {	//	calls the main
		main(null);
	}
	
	public static void printChain() {	//	displays hash codes and consecutive blocks
		int i=0;
		while(i < blockchain.size()) {
			System.out.println("Block#: "+ i + " "+ blockchain.get(i++));
		}
	}

	public static Boolean chainValidityCheck () {	//	old verification method later replaced by the one below
		Block cB; 
		Block pB;

		int i = 1;
		while (i < blockchain.size()) {
			cB = blockchain.get(i);
			pB = blockchain.get(i-1);
			if(!cB.hash.equals(cB.calculateHash()) ){
				System.out.println("The Current Hashes are not equal");			
				return false;
			}
			if(!pB.hash.equals(cB.previousHash) ) {
				System.out.println("The previous Hashes are not equal");
				return false;
			}
			i++;
		}
		return true;
	}
	
	public static Boolean isChainValid() {
		//	https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce
		//	Website above was used in getting the isChainValid method working
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOut> tempTransOut = new HashMap<String,TransactionOut>(); //a temporary working list of unspent transactions at a given block state.
//		System.out.println(unTransOut.get(genesis.reciept.get(0).id));	//	checks for issues with genesis
//		System.out.println(genesis.reciept.get(0).parentTransactionId);
//		System.out.println(genesis.sender.equals(ragnarok.pubk));
//		System.out.println(genesis.recipient.equals(gen.pubk));
		tempTransOut.put(genesis.reciept.get(0).id, genesis.reciept.get(0));
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {	//	loop through the blockchain
			
			currentBlock = blockchain.get(i);	//	obtain data from two consecutive blocks
			previousBlock = blockchain.get(i-1);
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){	//	check for mismatching hashes
				System.out.println("Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("Unmined block error");
				return false;
			}
			
			TransactionOut tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) {	//	loop through the transactions of a block
				Transaction currentTransaction = currentBlock.transactions.get(t);	//	get the currentTransaction
				
				if(!currentTransaction.verifySig()) {	//	Is transaction signed?
					System.out.println("Invalid signature on Transaction(" + t + ")");
					return false; 
				}
				if(currentTransaction.getInValue() != currentTransaction.getOutValue()) {	//	is the total input equal to the total output?
					System.out.println("Unbalanced inputs and outputs on Transaction(" + t + ")");
					return false; 
				}
				for(TransactionOut output: currentTransaction.reciept) {	//	for each output transaction in the arraylist
					tempTransOut.put(output.id, output);	//	adds the output transactions to a hashmap
				}
				
				if( currentTransaction.reciept.get(0).reciepient != currentTransaction.recipient) {
					System.out.println("Invalid recipient for Transaction(" + t + ")");
					return false;	//	checks if output data is valid
				}
				if( currentTransaction.reciept.get(1).reciepient != currentTransaction.sender) {
					System.out.println("Invalid senter for Transaction(" + t + ")");
					return false;	//	
				}
				
			}
			
		}
		//	System.out.println("Blockchain is valid");	//	Not relevant, and somewhat annoying, post-testing
		return true;
	}

	public static void addBlock(Block newBlock) {	//	adds a new block to the chain
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}


