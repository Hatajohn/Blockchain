import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

class ObjectTesting {

	@Test
	void testMain() throws NoSuchAlgorithmException {
		System.out.println("First coin: ");	//	create instance of Coin
		Coin c1 = new Coin();
		c1.getId();
		
		assertTrue(c1 != null);
		
		Coin c2 = new Coin();	//	 two more Coins
		Coin c3 = new Coin();
		
		assertTrue(c2 != null);
		assertTrue(c3 != null);
		
		assertFalse(c1.equals(c2));	//	Check that coin ids are different
		assertFalse(c2.equals(c3));
		assertFalse(c1.equals(c3));
		
		System.out.println("\nWallet test: ");
		Wallet w = new Wallet();	//	 give Patrick Star a wallet
		assertTrue(w.isEmpty());	//	Wallet should be empty on initialization
		assertTrue(w.store(c1));	//	add the coins to the wallet
		assertTrue(w.store(c2));
		assertTrue(w.store(c3));
		
		assertTrue(c1.getOwner().equals(w.getPublic()));	//	Show that the coins are currently owned by Patrick Star
		assertTrue(c2.getOwner().equals(w.getPublic()));
		assertTrue(c3.getOwner().equals(w.getPublic()));
		
		w.open();	//	Display the wallet's contents
		System.out.println("BALANCE = " + w.getBalance());
		
		assertTrue(w.removeC(c1));	//	Empty the wallet
		assertTrue(w.removeC(c2));
		assertTrue(w.removeC(c3));
		assertTrue(w.isEmpty());	//	Show that the wallet is empty
	}

}
