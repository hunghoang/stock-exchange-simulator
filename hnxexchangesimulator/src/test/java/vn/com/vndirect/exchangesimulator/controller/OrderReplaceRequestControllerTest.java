package vn.com.vndirect.exchangesimulator.controller;

import java.io.IOException;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.SocketClient;
import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderReplaceRequestQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

public class OrderReplaceRequestControllerTest {
	
	private OrderReplaceRequestController orderReplaceRequestController;
	private QueueOutService queueOutService;
	private InMemory memory;
	private OrderReplaceRequestQueue  orderReplaceRequestQueue;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		orderReplaceRequestQueue = new OrderReplaceRequestQueue();
		queueOutService = new QueueOutServiceImpl();
		orderReplaceRequestController = new OrderReplaceRequestController(orderReplaceRequestQueue, queueOutService, memory);
	}

	@Test
	public void testReceiveExecutionReportWhenOrderReplaceRequest() throws InterruptedException, IOException {
		memory.put("SocketClient", "VNDS", new SocketClient(new Socket(), "VNDS"));
		memory.put("last_processed_sequence", "VNDS", 0);
		ExecutionReport rootOrderReport = new ExecutionReport();
		rootOrderReport.setClOrdID("KLSVNDS1223333");
		rootOrderReport.setAccount("1233333");
		rootOrderReport.setSide('0');
		rootOrderReport.setOrigClOrdID("KLSVNDS1223333");
		rootOrderReport.setOrdType('2');
		rootOrderReport.setLastQty(0);
		rootOrderReport.setLastPx(0);
		rootOrderReport.setOrderID("KLSVNDS12345678");
		memory.put("ExecutionReport",rootOrderReport.getOrigClOrdID() , rootOrderReport);
		
		OrderReplaceRequest order = new OrderReplaceRequest();
		order.setSenderCompID("VNDS");
		order.setSymbol("KLS");
		order.setClOrdID("g1223333");
		order.setOrigClOrdID("KLSVNDS1223333");
		order.setPrice(1600);
		order.setOrderQty(2000);
		orderReplaceRequestQueue.add(order);
		Thread.sleep(2000);
		ExecutionReport msg = (ExecutionReport) queueOutService.poll();
		Assert.assertNotNull(msg);
		Assert.assertEquals("VNDS", msg.getTargetCompID());
		Assert.assertEquals("KLS", msg.getSymbol());
		Assert.assertEquals('2', msg.getOrdType());
		Assert.assertEquals(1600, msg.getPrice(), 0.0);
		Assert.assertEquals(2000, msg.getOrderQty());
		Assert.assertEquals("1233333", msg.getAccount());
		Assert.assertEquals("KLSVNDS1223333", msg.getOrderID());
		System.out.println(new FixConvertor().convertObjectToFix(msg));
		orderReplaceRequestController.stop();
	}

}
