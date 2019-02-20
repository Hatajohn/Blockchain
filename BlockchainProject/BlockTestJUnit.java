import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

//Group4
//JUnit test for individual blocks and chain


public class BlockTestJUnit {

	//Tests if genesis block is created correctly
	
	Block test = new Block("Test");
	Block test2 = new Block("Test2");
	@Test
	public void isGenBlockCreated() {
		assertEquals("0",test.previousHash);
	    assertEquals("Test", test.data);
	}
	//Tests if a second block is created correctly
	@Test
	public void isSecondBlockCreated() {
		assertEquals(test.hash,test2.previousHash);
		 assertEquals("Test2", test2.data);
	}
//Tests if the hashes are correct (next block contains previous hash)
	@Test
	public void chainIsCorrect() throws NoSuchAlgorithmException {
		Chain chain = new Chain();
		int count = 0;
		System.out.print(count++);
		chain.blockchain.add(new Block("0"));		
		System.out.print(count++);
		chain.blockchain.add(new Block(chain.blockchain.get(chain.blockchain.size()-1).hash)); 
		System.out.print(count++);
		chain.blockchain.add(new Block(chain.blockchain.get(chain.blockchain.size()-1).hash));
		System.out.print(count++);
		
		assertEquals(chain.blockchain.get(0).hash,chain.blockchain.get(1).previousHash);
		assertEquals(chain.blockchain.get(1).hash,chain.blockchain.get(2).previousHash);
		
	}

}
