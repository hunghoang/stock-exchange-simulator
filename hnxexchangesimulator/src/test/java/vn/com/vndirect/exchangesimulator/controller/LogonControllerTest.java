package vn.com.vndirect.exchangesimulator.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.net.Socket;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.SocketClient;
import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.LogonQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.model.Logon;

public class LogonControllerTest {

	private LogonController logonController;
	private QueueOutService queueOutService;
	private InMemory memory;
	private LogonQueue logonQueue;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		logonQueue = new LogonQueue();
		queueOutService = new QueueOutServiceImpl();
		logonController = new LogonController(logonQueue, queueOutService, memory);
	}
	
	@Test
	public void testGenerateLogon() {
		Logon logon = new Logon();
		logon.setSenderCompID("VNDS");
		Logon acceptLogon = logonController.generateAcceptLogon(logon);
		String expectedTargetCompId = "VNDS";
		String expectedAcceptLogon = "Accept logon";
		assertEquals(expectedTargetCompId, acceptLogon.getTargetCompID());
		assertEquals(expectedAcceptLogon, acceptLogon.getText());
	}
	
	@Test
	public void testReceiveLogon() throws InterruptedException {
		Object clients = memory.get("SocketClientList", "");
		assertNull(clients);
		memory.put("SocketClient", "VNDS", new SocketClient(new Socket(), "VNDS"));
		Logon logon = new Logon();
		logon.setSenderCompID("VNDS");
		logon = logonController.process(logon);
		logonController.response(logon);
		clients = memory.get("SocketClientList", "");
		assertNotNull(clients);
		assertEquals(1, ((List) clients).size());
		Thread.sleep(1000);
		String expectedTargetCompId = "VNDS";
		String expectedAcceptLogon = "Accept logon";
		Logon successfulllogon = (Logon) queueOutService.peek();
		assertEquals(expectedTargetCompId, successfulllogon.getTargetCompID());
		assertEquals(expectedAcceptLogon, successfulllogon.getText());
		
		SocketClient client = (SocketClient) memory.get("SocketClient", "VNDS");
		assertEquals(true, client.isLogon());
	}
	
	@Test
	public void testReceiveLogonWhenLogonQueueHasMessage() throws InterruptedException {
		memory.put("SocketClient", "VNDS", new SocketClient(new Socket(), "VNDS"));
		Logon logon = new Logon();
		logon.setSenderCompID("VNDS");
		logonQueue.add(logon);
		Thread.sleep(1000);
		String expectedTargetCompId = "VNDS";
		String expectedAcceptLogon = "Accept logon";
		Logon successfulllogon = (Logon) queueOutService.peek();
		assertEquals(expectedTargetCompId, successfulllogon.getTargetCompID());
		assertEquals(expectedAcceptLogon, successfulllogon.getText());
		
		SocketClient client = (SocketClient) memory.get("SocketClient", "VNDS");
		assertEquals(true, client.isLogon());
		logonController.stop();
	}
}
