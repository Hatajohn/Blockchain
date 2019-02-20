import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class TransactionJUnit {

	@Test
	public void test() throws NoSuchAlgorithmException {
		Wallet walletA = new Wallet();	//	create both wallets for the test
		Wallet walletB = new Wallet();
		System.out.println("Private and public keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.prik));	//	Display the private key that will be used
		System.out.println(StringUtil.getStringFromKey(walletA.pubk));	//	Display the public key that will be used
		Transaction transaction = new Transaction(walletA.pubk, walletB.pubk, 5, null);	//	generate transaction between A and B 
																						//	using their public keys; the value is negligible
		transaction.genSig(walletA.prik);	//	sign transaction with A's private key

		assertTrue(transaction.verifySig());	//	check that transaction was verified
		
		Transaction transaction2 = new Transaction(walletB.pubk, walletA.pubk, 20, null);
		transaction2.genSig(walletB.prik);
		
		assertTrue(transaction2.verifySig());
		
		Wallet walletC = new Wallet();
		
		Transaction transaction3 = new Transaction(walletA.pubk, walletC.pubk, 20, null);	// Same wallet can be used in multiple transactions
		transaction3.genSig(walletA.prik);
		
		assertTrue(transaction3.verifySig());
		
		assertTrue(walletA.getPublic() != walletB.getPublic());	//	Wallets are different
		assertTrue(walletB.getPublic() != walletC.getPublic());
		assertTrue(walletA.getPublic() != walletC.getPublic());
	}

}
