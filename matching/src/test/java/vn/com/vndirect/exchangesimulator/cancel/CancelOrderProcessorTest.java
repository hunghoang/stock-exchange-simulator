package vn.com.vndirect.exchangesimulator.cancel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;

public class CancelOrderProcessorTest {
	
	private static final String ACCOUNT = "021C090823";
	private static final String VND = "VND";
	
	private CancelOrderProcessor cancelOrderProcessor;
	private InMemory memory;
	private ExecutionReportQueue queueOut;
	
	@Before
	public void setup() {
		memory = new InMemory();
		queueOut = new ExecutionReportQueue();
		cancelOrderProcessor = new CancelOrderProcessor(memory, queueOut);
	}
	private NewOrderSingle createSampleOrder(String clOrdId) {
		NewOrderSingle order = new NewOrderSingle();
		order.setSenderCompID("VNDS");
		order.setClOrdID(clOrdId);
		order.setPrice(1000d);
		order.setOrderQty(30);
		order.setSide('1');
		order.setOrdType('2');
		order.setSymbol(VND);
		order.setAccount(ACCOUNT);
		order.setOrderId("orderId");
		return order;
	}
	
	
	@Test
	public void testGenerateExcutionReportForCanceledOrder() {
		String clOrdId = "clordid";
		NewOrderSingle order = createSampleOrder(clOrdId);
		OrderCancelRequest request = new OrderCancelRequest();
		String orderId = order.getOrderId();
		request.setClOrdID("newClOrdId");
		request.setOrigClOrdID(orderId);
		memory.put(NewOrderSingle.class.getSimpleName(), orderId, order);
		
		ExecutionReport report = cancelOrderProcessor.generate(request);
		assertNotNull(report);
		assertEquals(report.getTargetCompID(), order.getSenderCompID());
		assertEquals('4', report.getExecType());
		assertEquals('3', report.getOrdStatus());
		assertNotNull(report.getOrderID());
		assertEquals(30, report.getLeavesQty());
		assertEquals("newClOrdId", report.getClOrdID());
		assertEquals("newClOrdId", report.getOrigClOrdID());
		assertEquals(VND, report.getSymbol());
		assertEquals('1', report.getSide());
		assertEquals('2', report.getOrdType());
		assertEquals(ACCOUNT, report.getAccount());
	}
	
	@Test
	public void testCancelOrderProcessorPushMessageToQueueout() {
		
	}
	
	@Test
	public void testGenerateExcutionReportWhenCancelRequestInvalid() {
		String clOrdId = "clordid";
		OrderCancelRequest request = new OrderCancelRequest();
		request.setClOrdID("newClOrdId");
		request.setOrigClOrdID(clOrdId);
		ExecutionReport report = cancelOrderProcessor.generate(request);
		assertNull("Account is not null", report.getAccount());
		assertNull("ClOrdId is not null", report.getClOrdID());
	}
}
