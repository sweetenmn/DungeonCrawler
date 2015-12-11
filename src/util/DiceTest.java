package util;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class DiceTest {

	@Test
	public void test() {
		ArrayList<Integer> results = new ArrayList<Integer>();
		Dice dice = new Dice(12);
		for (int i = 0; i < 500; i++){
			results.add(dice.roll());
		}
		assertTrue(results.contains(11));
		assertFalse(results.contains(12));
	}

}
