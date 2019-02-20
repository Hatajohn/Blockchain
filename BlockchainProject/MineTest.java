import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MineTest {
	Block test = new Block("Test", "0");
	Block test2 = new Block("Test2", test.hash);
	Block test3 = new Block("Test3", test2.hash);

	
	@Test
	void isMinedBlock1() {
		test.mineBlock(3);
		assertEquals(test.hash.substring(0,3),"000");
	}
	@Test
	void isMinedBlock2() {
		test2.mineBlock(3);
		assertEquals(test2.hash.substring(0,3),"000");
	}
	@Test
	void isMinedBlock3Difficult4() {
		test3.mineBlock(4);
		assertEquals(test3.hash.substring(0,4),"0000");
	}
}
