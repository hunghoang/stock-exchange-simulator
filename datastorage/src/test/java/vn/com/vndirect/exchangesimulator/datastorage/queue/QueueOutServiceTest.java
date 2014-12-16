package vn.com.vndirect.exchangesimulator.datastorage.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class QueueOutServiceTest {

	private QueueOutService<Object> service;
	
	@Before
	public void setUp() {
		service = new QueueOutServiceImpl();
	}
	
	@Test
	public void testAdd() {
		service.add("a");
		service.add("a");
		
		assertEquals(2, service.size());
	}
	
	@Test
	public void testAddThenPoll() {
		service.add("c");
		service.add("a");
		service.add("b");
		
		assertEquals("c", service.poll());
		assertEquals(2, service.size());
	}
	
	@Test
	public void testPollFromAnEmptyQueue() {
		assertNull(service.poll());
	}
	
	@Test
	public void testPeekExist() {
		service.add("a");
		service.add("b");
		service.add("c");
		
		assertEquals("a", service.peek());
		assertEquals("a", service.peek());
		assertEquals(3, service.size());
	}
	
	@Test
	public void testPeekEmpty() {
		assertNull(service.peek());
	}
	
}
