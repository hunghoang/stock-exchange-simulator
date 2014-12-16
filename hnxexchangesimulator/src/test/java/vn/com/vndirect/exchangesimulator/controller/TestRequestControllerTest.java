package vn.com.vndirect.exchangesimulator.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.datastorage.queue.TestRequestQueue;
import vn.com.vndirect.exchangesimulator.model.Heartbeat;
import vn.com.vndirect.exchangesimulator.model.TestRequest;

public class TestRequestControllerTest {

	private TestRequestController controller;
	private QueueOutService queueOutService;
	private TestRequestQueue queueIn;
	
	@Before
	public void setUp() {
		queueIn = new TestRequestQueue();
		queueOutService = new QueueOutServiceImpl();
		controller = new TestRequestController(queueIn, queueOutService);
	}
	
	@Test
	public void testGetSecurityStatusMessage() throws InterruptedException {
		TestRequest req = new TestRequest();
		req.setSenderCompID("VNDS");
		queueIn.add(req);
		Thread.sleep(500);
		Heartbeat msg = (Heartbeat) queueOutService.poll();
		Assert.assertEquals("VNDS", msg.getTargetCompID());
		Assert.assertNotNull(msg);
		controller.stop();
	}
}
