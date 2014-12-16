package vn.com.vndirect.exchangesimulator.datastorage.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class InMemoryTest {
	
	private InMemory memory;
	
	@Before
	public void setUp() {
		memory = new InMemory();
	}

	@Test
	public void testPutNotExistKey() {
		memory.put("type", "key", "value");
		
		Object value = memory.get("type", "key");
		
		assertEquals("value", value);
	}
	
	@Test
	public void testPutExistKey() {
		memory.put("type", "key", "value1");
		memory.put("type", "key", "value2");
		
		Object value = memory.get("type", "key");
		
		assertEquals("value2", value);
	}

	@Test
	public void testRemove() {
		memory.put("type", "key", "value1");
		
		memory.remove("type", "key");
		
		Object value = memory.get("type", "key");
		
		assertNull(value);
	}
}
