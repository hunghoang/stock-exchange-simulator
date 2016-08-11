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
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.validator.NewOrderSingleValidator;
import vn.com.vndirect.exchangesimulator.validator.PriceValidator;

public class OrderReplaceRequestProcessorTest {
	
	private ReplaceOrderProcessor replaceOrderProcessor;
	
	private OrderStorage storage;
	
	private Matcher matcher;
	
	@Before
	public void setUp() {
		storage = new OrderStorage();
		matcher = Mockito.mock(Matcher.class);
		NewOrderSingleValidator validator = Mockito.mock(NewOrderSingleValidator.class);
		replaceOrderProcessor = new ReplaceOrderProcessor(storage, matcher, validator) {
			protected boolean validateRules(OrderReplaceRequest request, NewOrderSingle origOrder) throws vn.com.vndirect.exchangesimulator.validator.exception.ValidateException {
				return true;
			}
		};
	}
	
	@Test
	public void testProcessorGenerateReportWhenIncreasePrice() {
		NewOrderSingle order = createNewOrder();
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(5000);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		
		verifyReport(report, order.getOrderQty(), 0, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotNull("new Order is not created when increase price" , newOrder);
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0.1);
		Assert.assertEquals(order.getOrderQty(), newOrder.getOrderQty());
	}
	
	@Test
	public void testProcessorGenerateReportWhenDecreasePrice() {
		NewOrderSingle order = createNewOrder();
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() - 1000);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, order.getOrderQty(), 0, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotNull("new Order is not created when decrease price" , newOrder);
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0.1);
		Assert.assertEquals(order.getOrderQty(), newOrder.getOrderQty());
	}
	
	@Test
	public void testProcessorGenerateReportWhenIncreaseQuantity() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setOrderQty(8000);
		request.setCashOrderQty(12000);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);

		
		ExecutionReport report = reports.get(0);
		verifyReport(report, 12000, 4000, order.getPrice());
		verifyClOrdId(request, report);
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("newOrder is not create when replace order with new one having decreased quantity and price", order, newOrder);
		Assert.assertEquals(12000, newOrder.getOrderQty());
		Assert.assertEquals(order.getPrice(), newOrder.getPrice(), 0.1);
	}
	
	@Test
	public void testProcessorGenerateReportWhenDecreaseQuantity() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(6000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(4000);
		request.setOrderQty(6000);
		request.setCashOrderQty(3000);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, 3000, -3000, order.getPrice());
		verifyClOrdId(request, report);
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertEquals(order, newOrder);
		Assert.assertEquals(3000, newOrder.getOrderQty());
	}
	
	
	@Test
	public void testProcessorGenerateReportWhenIncreaseQuantityAndPrice() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setOrderQty(8000);
		request.setCashOrderQty(12000);
		request.setPrice(order.getPrice() + 1000);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, 12000, 4000, request.getPrice());
		verifyClOrdId(request, report);
				
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("newOrder is not created when replace order with new one having increase quantity and price", order, newOrder);
		Assert.assertEquals(12000, newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
	}
	
	@Test
	public void testProcessorGenerateReportWhenIncreasePriceAndDecreaseQuantity() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() + 1000);
		request.setOrderQty(order.getOrderQty());
		request.setCashOrderQty(order.getOrderQty() - 1500);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		
		ExecutionReport report = reports.get(0);
		verifyReport(report, request.getCashOrderQty(), -1500, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("newOrder is not created when replace order with new one having increase  and decrease quantity", order, newOrder);
		Assert.assertEquals(request.getCashOrderQty(), newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
	}
	
	@Test
	public void testProcessorGenerateReportWhenIncreaseQuantityAndDecreasePrice() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() - 1000);
		request.setOrderQty(order.getOrderQty());
		request.setCashOrderQty(order.getOrderQty() + 2500);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		
		ExecutionReport report = reports.get(0);
		verifyReport(report, request.getCashOrderQty(), 2500, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("newOrder is not created when replace order with new one having increase quantity and decrease price", order, newOrder);
		Assert.assertEquals(request.getCashOrderQty(), newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
	}
	
	@Test
	public void testProcessorGenerateReportWhenDecreaseQuantityAndPrice() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() - 1000);
		request.setOrderQty(order.getOrderQty());
		request.setCashOrderQty(order.getOrderQty() - 2500);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		
		ExecutionReport report = reports.get(0);
		verifyReport(report, request.getCashOrderQty(), -2500, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("newOrder is not created when replace order with new one having increase quantity and price", order, newOrder);
		Assert.assertEquals(request.getCashOrderQty(), newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
	}
	
	@Test
	public void testProcessorGenerateReportWhenIncreaseQuantityAndOrderIsPartialFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(7000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(4000);
		// Expected update order quantity = 10000 but order is partial filled with cum quantity = 1000
		request.setOrderQty(7000);
		request.setCashOrderQty(9000);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, 9000, 2000, order.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("new Order is not created when increase quantity and order is partial filled", order, newOrder);
		Assert.assertEquals(9000, newOrder.getOrderQty());
	}
	
	@Test
	public void testProcessorGenerateReportWhenDecreaseQuantityAndOrderIsPartialFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(7000);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(4000);
		// Expected update order quantity = 7500 but order is partial filled with cum quantity = 1000
		request.setOrderQty(7000);
		request.setCashOrderQty(6500);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, 6500, -500, order.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertEquals(order, newOrder);
		Assert.assertEquals(6500, newOrder.getOrderQty());
	}
	
	@Test
	public void testProcessorGenerateReportWhenDecreaseQuantityAndOrderIsPartialFilled2() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(7000);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(4000);
		// Expected update order quantity = 6500 but order is partial filled with cum quantity = 1000
		request.setOrderQty(8000);
		request.setCashOrderQty(6500);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, 5500, -1500, order.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertEquals(order, newOrder);
		Assert.assertEquals(5500, newOrder.getOrderQty());
	}
	
	@Test
	public void testProcessorGenerateReportWhenIncreasePriceAndOrderIsPartialFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(7000);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() + 2000);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		verifyReport(report, 7000, 0, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("new order is not created when increase price", order, newOrder);
		Assert.assertEquals(7000, newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
	}
	
	
	@Test
	public void testGenerateReportWhenDecreasePriceAndOrderIsPartialFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		order.setOrderQty(4300);
		storage.add(order);
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() - 1600);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		
		ExecutionReport report = reports.get(0);
		verifyReport(report, 4300, 0, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("new order is not created when increase price", order, newOrder);
		Assert.assertEquals(4300, newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
	}
	

	@Test
	public void testGeneratorReportWhenDecreaseQuantitySmallerThanCumQuantity() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		int partialFilledQuantity = 3100;
		order.setOrderQty(order.getOrderQty() - partialFilledQuantity);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice());
		request.setOrderQty(8000);
		request.setCashOrderQty(partialFilledQuantity - 1000);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		
		ExecutionReport report = reports.get(0);
		VerifyRejectReport.verify(report);
		verifyClOrdId(request, report);
		Assert.assertNotNull(report.getText());
	}
	
	@Test
	public void testGenerateReportWhenIncreaseQuantityAndPriceAndOrderIsPartialFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		// Partial filled
		int partialFilledQuantity = 3100;
		order.setOrderQty(order.getOrderQty() - partialFilledQuantity);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() + 1000);
		request.setOrderQty(order.getOrderQty());
		request.setCashOrderQty(order.getOrderQty() + 1200);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		
		ExecutionReport report = reports.get(0);
		verifyReport(report, request.getCashOrderQty(), 1200, request.getPrice());
		verifyClOrdId(request, report);
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("new order is not created when increase price", order, newOrder);
		Assert.assertEquals(request.getCashOrderQty(), newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);	
	}

	@Test
	public void testGenerateReportWhenIncreasePriceAndOrderIsFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(0);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() + 1000);
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		
		VerifyRejectReport.verify(report);
		verifyClOrdId(request, report);
		
	}
	
	@Test
	public void testGenerateReportWhenIncreaseQuantityAndOrderIsFilled() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(0);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice());
		request.setOrderQty(1000);
		request.setCashOrderQty(2000);

		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		
		VerifyRejectReport.verify(report);
		verifyClOrdId(request, report);
		
	}
	
	
	@Test
	public void testGenerateReportWhenMultipleReplaceOrder() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(8000);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setPrice(order.getPrice() + 1000);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		
		verifyReport(report, order.getOrderQty(), 0, request.getPrice());
		
		String newOrderId = order.getOrderId();
		NewOrderSingle newOrder = storage.get(newOrderId);
		Assert.assertNotEquals("new order is not created when increase price", order, newOrder);
		Assert.assertEquals(order.getOrderQty(), newOrder.getOrderQty());
		Assert.assertEquals(request.getPrice(), newOrder.getPrice(), 0);
		
		Assert.assertEquals(report.getOrderID(), newOrder.getOrderId() + 1);
		Assert.assertEquals(report.getClOrdID(),request.getClOrdID());
		Assert.assertEquals(report.getOrigClOrdID(), request.getClOrdID());
		
		OrderReplaceRequest request2 = createReplaceRequest();
		request2.setPrice(newOrder.getPrice());
		request2.setClOrdID(newOrder.getOrderId());
		request2.setOrigClOrdID(newOrder.getOrderId());
		request2.setOrderQty(newOrder.getOrderQty());
		request2.setCashOrderQty(newOrder.getOrderQty() + 1000);
		
		List<ExecutionReport> reports2 = replaceOrderProcessor.process(request2);
		ExecutionReport report2 = reports2.get(0);
		
		verifyReport(report2, request2.getCashOrderQty(), 1000, request.getPrice());
		
		String newOrderId2 = newOrder.getOrderId();
		NewOrderSingle newOrder2 = storage.get(newOrderId2);
		Assert.assertNotEquals("new order is not created when increase price", newOrder, newOrder2);
		Assert.assertEquals(request2.getCashOrderQty(), newOrder2.getOrderQty());
		Assert.assertEquals(newOrder.getPrice(), newOrder2.getPrice(), 0);
		
		Assert.assertEquals(report2.getOrderID(), newOrder2.getOrderId() + 1);
		Assert.assertEquals(report2.getClOrdID(),request2.getClOrdID());
		Assert.assertEquals(report2.getOrigClOrdID(), request2.getClOrdID());
		
	}
	
	@Test
	public void testGenerateReplaceReportWhenReplaceOrderWithExpectedDecreasedQuantityBiggerThanRemainQuantity() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(200);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setOrderQty(1000);
		request.setCashOrderQty(700);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		VerifyRejectReport.verify(report);
		
	}
	
	@Test
	public void testGenerateReplaceReportWhenReplaceOrderWithExpectedDecreasedQuantityEqualRemainQuantity() {
		NewOrderSingle order = createNewOrder();
		order.setOrderQty(200);
		storage.add(order);
		
		OrderReplaceRequest request = createReplaceRequest();
		request.setOrderQty(1000);
		request.setCashOrderQty(800);
		
		List<ExecutionReport> reports = replaceOrderProcessor.process(request);
		ExecutionReport report = reports.get(0);
		VerifyRejectReport.verify(report);
		
	}
	
	private void verifyReport(ExecutionReport report, int lastQty, int leaveQty, double lastPx) {
		Assert.assertEquals("last quantity is invalid", lastQty, report.getLastQty());
		Assert.assertEquals("leave quantity is invalid", leaveQty, report.getLeavesQty());
		Assert.assertEquals("last price is invalid", lastPx, report.getLastPx(), 0);
		Assert.assertEquals('5', report.getExecType()); 
	}
	
	private void verifyClOrdId(OrderReplaceRequest request, ExecutionReport report) {
		Assert.assertEquals(request.getClOrdID(), report.getClOrdID());
	}
	
	private OrderReplaceRequest createReplaceRequest() {
		OrderReplaceRequest request = new OrderReplaceRequest();
		request.setMsgSeqNum(1);
		request.setClOrdID("orderid");
		request.setOrigClOrdID("orderid012");
		request.setSymbol("VND");
		request.setPrice(4000);
		return request;
	}

	private NewOrderSingle createNewOrder() {
		NewOrderSingle order = new NewOrderSingle();
		order.setSide('1');
		order.setClOrdID("clordid");
		order.setOrderId("orderid012");
		order.setOrdType('2');
		order.setPrice(4000);
		order.setOrderQty(300);
		return order;
	}
	
	
}
