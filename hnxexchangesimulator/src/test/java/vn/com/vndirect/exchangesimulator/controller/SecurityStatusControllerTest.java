package vn.com.vndirect.exchangesimulator.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.datastorage.queue.SecurityStatusRequestQueue;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.SecurityStatusManager;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;

public class SecurityStatusControllerTest {
	
	private QueueOutService queueOutService;
	private SecurityStatusRequestQueue queueIn;
	private InMemory memory;
	private SecurityStatusController securityStatusController;
	private SecurityStatusManager securityStatusManager;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		securityStatusManager = new SecurityStatusManager() {
			@Override
			public List<SecurityStatus> getSecurityStatus() {
				List<SecurityStatus> securityStatusList = new ArrayList<SecurityStatus>();
				SecurityStatus securityStatus = new SecurityStatus();
				securityStatus.setSymbol("VND");
				securityStatusList.add(securityStatus);
				return securityStatusList;
			}

			@Override
			public void setInmemory(InMemory inMemory) {
				
			}
		};
		queueOutService = new QueueOutServiceImpl();
		queueIn = new SecurityStatusRequestQueue();
		securityStatusController = new SecurityStatusController(queueIn, queueOutService, securityStatusManager, memory);
		memory.put("last_processed_sequence", "VNDS", 0);
	}
	
	@Test
	public void testGetSecurityStatusMessage() throws InterruptedException {
		SecurityStatusRequest req = new SecurityStatusRequest();
		req.setSenderCompID("VNDS");
		queueIn.add(req);
		securityStatusController.start();
		Thread.sleep(500);
		SecurityStatus msg = (SecurityStatus) queueOutService.poll();
		Assert.assertNotNull("Symbol is null", msg.getSymbol());
		Assert.assertEquals("VNDS", msg.getTargetCompID());
		Assert.assertNotNull(msg);
		securityStatusController.stop();
	}
}