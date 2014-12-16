package vn.com.vndirect.exchangesimulator.matching;

import static org.junit.Assert.*;

import org.junit.Test;

public class SequencerTest {

	@Test
	public void testGenCorrectLength() {
		int totalLength = 6;
		Sequencer seq = new Sequencer("VND", totalLength);
		assertEquals(totalLength, seq.next().length());
		assertEquals("VND002", seq.next());
	}

	@Test
	public void testIncreaseSeqAfterNext(){
		Sequencer seq = new Sequencer("VND", 6);
		seq.next(); seq.next();
		assertEquals("VND003", seq.next());
	}
	
	@Test
	public void testRotateBackToOneAfterSeqOverflow(){
		Sequencer seq = new Sequencer("VND", 5);
		for(int i = 0; i < 99; i++){
			seq.next();
		}
		assertEquals("VND01", seq.next());
	}
}
