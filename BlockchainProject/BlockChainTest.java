import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class BlockChainTest {
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	static int difficulty = 3;
	public static float minimumTransaction = 0.1f;
	public static HashMap<String, TransactionOut> unTransOut = new HashMap<String, TransactionOut>();
	public static Wallet test1;
	public static Wallet test2;
	public static Transaction genesis;
	
	
	
	
	
	@SuppressWarnings({ "deprecation", "unlikely-arg-type" })
	@Test
	public void testWalletA() throws NoSuchAlgorithmException {
    Chain genesis = new Chain();

	equals(genesis.test1.getBalance()==80);	
	for (int i = 0; i < 50; ++i) System.out.println();
}   @Test
	public void testWalletB() throws NoSuchAlgorithmException {
	    Chain genesis = new Chain();

		equals(genesis.test2.getBalance()==20);	
		for (int i = 0; i < 50; ++i) System.out.println();
	}
    @Test
	public void testWalletOverDraw() throws NoSuchAlgorithmException {
	    Chain genesis = new Chain();

		equals(genesis.testPointer==false);	
	}
}
