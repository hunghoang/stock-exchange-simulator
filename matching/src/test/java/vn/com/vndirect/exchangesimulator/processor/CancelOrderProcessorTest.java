package vn.com.vndirect.exchangesimulator.processor;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;

public class CancelOrderProcessorTest {

	/*
	 * 1. Huy lenh chua khop thanh cong 
	 * 2. Huy lenh khop 1 phan thanh cong
	 * 3. Huy lenh khop toan bo reject 
	 * 
	 * 4. Huy lenh (khi lenh bị reject - huy sai thong tin truyen vao ....)
	 * 5. Huy lenh da sua thanh cong
	 * 6. Huy lenh sua nhieu lan thanh cong
	 * 7. Huy lenh da huy bi reject 
	 * 8. Huy lenh sai phien
	 */

	private CancelOrderProcessor cancelOrderProcessor;

	private OrderStorage storage;

	private Matcher matcher;

	@Before
	public void setUp() {
		storage = new OrderStorage();
		matcher = Mockito.mock(Matcher.class);
		cancelOrderProcessor = new CancelOrderProcessor(storage, matcher);
	}

	@Test
	public void testGenerateCancelReport() {
		NewOrderSingle order = createNewOrder();
		storage.add(order);
		OrderCancelRequest request = createCancelRequest();
		List<ExecutionReport> reports = cancelOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyCancelReport(report, order);
	}
	
	@Test
	public void testGenerateCancelReportWhenCancelPartialFilledOrder() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(5000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(3000);
		storage.add(order);
		OrderCancelRequest request = createCancelRequest();
		List<ExecutionReport> reports = cancelOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyCancelReport(report, order);
	}
	
	@Test
	public void testGenerateCancelReportWhenCancelFilledOrder() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(5000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(0);
		storage.add(order);
		OrderCancelRequest request = createCancelRequest();
		List<ExecutionReport> reports = cancelOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyRejectReport(report, order);
	}

	@Test
	public void testGenerateCancelReportWhenOrderNotExist() {
		OrderCancelRequest request = createCancelRequest();
		List<ExecutionReport> reports = cancelOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		
		Assert.assertEquals("ExecType ", '8', report.getExecType());
		Assert.assertEquals("OrdStatus ", '8', report.getOrdStatus());
		Assert.assertEquals("OrdRejReason ", "5", report.getOrdRejReason());
		Assert.assertEquals("ClOrdId ", request.getClOrdID(), report.getClOrdID());
		Assert.assertEquals("ClOrdId ", request.getClOrdID(), report.getOrderID());
		
	}
	
	private void verifyRejectReport(ExecutionReport report, NewOrderSingle order) {
		VerifyRejectReport.verify(report);
		Assert.assertEquals("ClOrdId ", order.getOrderId(), report.getClOrdID());
		Assert.assertEquals("ClOrdId ", order.getOrderId(), report.getOrderID());
	}
	
	private void verifyCancelReport(ExecutionReport report, NewOrderSingle order) {
		Assert.assertEquals('3', report.getOrdStatus());
		Assert.assertEquals('4', report.getExecType());
		Assert.assertEquals(order.getOrderQty(), report.getLeavesQty());
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals(order.getSide(), report.getSide());
		Assert.assertEquals(order.getOrdType(), report.getOrdType());
		Assert.assertEquals(order.getPrice(), report.getPrice(), 0.0);
		Assert.assertEquals(order.getAccount(), report.getAccount());
	}

	private NewOrderSingle createNewOrder() {
		NewOrderSingle order = new NewOrderSingle();
		order.setSide('1');
		order.setClOrdID("clordid");
		order.setOrderId("orderid");
		order.setOrdType('2');
		order.setPrice(4000);
		order.setOrderQty(300);
		return order;
	}

	private OrderCancelRequest createCancelRequest() {
		OrderCancelRequest request = new OrderCancelRequest();
		request.setMsgSeqNum(1);
		request.setClOrdID("orderid");
		request.setOrigClOrdID("orderid");
		request.setSymbol("VND");
		return request;
	}

}
