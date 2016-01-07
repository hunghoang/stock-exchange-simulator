package vn.com.vndirect.exchangesimulator.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorageService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.CrossOrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.model.CrossOrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.GroupSide;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

public class CrossOrderControllerTest {

	private CrossOrderController orderController;

	private OrderStorageService orderStorageService = new OrderStorageService();
	
	private List<Object> responses = new ArrayList<Object>();
	
	private QueueOutService<Object> queueOut = new QueueOutServiceImpl() {
		@Override
		public boolean add(Object obj) {
			responses.add(obj);
			return true;
		};
	};
	
	@Before
	public void setup() throws Exception {
		orderController = new CrossOrderController(new CrossOrderQueue(), orderStorageService, new InMemory(), queueOut);
		responses.clear();
	}
	
	@Test
	public void testConfirmOneFirmOrder() {
		NewOrderCross order = createOneFirmOrder();
		orderController.onEvent(order);
		orderController.acceptOneFirm(order.getCrossID());
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertNotNull(report);
		Assert.assertEquals(order.getSenderCompID(), report.getTargetCompID());
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals("VND", report.getSymbol());
		Assert.assertEquals(ExecType.FILL, report.getExecType());
		Assert.assertEquals(OrdStatus.FILL, report.getOrdStatus());
		Assert.assertEquals("clid1", report.getClOrdID());
		Assert.assertEquals(report.getOrigClOrdID(), report.getOrderID());
		Assert.assertEquals(order.getGroupSides().get(0).getClOrdID(), report.getClOrdID());
		Assert.assertEquals("FILL", order.getCurrentStatus());
	}

	@Test
	public void testRejectOneFirmOrder() {
		NewOrderCross order = createOneFirmOrder();
		orderController.onEvent(order);
		String rejectCode = "-11003";
		orderController.rejectFromExchange(order.getCrossID(), rejectCode);
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertNotNull(report);
		Assert.assertEquals(order.getSenderCompID(), report.getTargetCompID());
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals("VND", report.getSymbol());
		Assert.assertEquals("3", report.getMsgType());
		Assert.assertEquals(1, report.getRefSeqNum());
		Assert.assertEquals("s", report.getRefMsgType());
		Assert.assertEquals(rejectCode, report.getSessionRejectReason());
	}

	@Test
	public void testSendCancelAckOneFirmOrder() {
		NewOrderCross order = createOneFirmOrder();
		orderController.onEvent(order);
		CrossOrderCancelRequest cancelOrder = createCancelCrossOrder();
		cancelOrder.setCrossType("1");
		orderController.sendCancelAck(cancelOrder);
		CrossOrderCancelRequest cancelOrderResponse = (CrossOrderCancelRequest) responses.get(0);
		verifyCancelAck(order, cancelOrderResponse);
	}

	private void verifyCancelAck(NewOrderCross order, CrossOrderCancelRequest cancelOrderResponse) {
		Assert.assertNotNull(cancelOrderResponse);
		Assert.assertNotNull(cancelOrderResponse.getTargetCompID());
		Assert.assertEquals("VND", cancelOrderResponse.getSymbol());
		Assert.assertEquals(order.getCrossID(), cancelOrderResponse.getCrossID());
		Assert.assertEquals("1", cancelOrderResponse.getCrossType());
		Assert.assertEquals(order.getCrossID(), cancelOrderResponse.getOrigCrossID());
		Assert.assertEquals(cancelOrderResponse.getOrderId(), cancelOrderResponse.getOrigCrossID());
		Assert.assertEquals("PENDING CANCEL", order.getCurrentStatus());
	}

	@Test
	public void testAcceptCancelOneFirmOrder() {
		NewOrderCross order = createOneFirmOrder();
		orderController.onEvent(order);
		orderController.acceptCancelOneFirm(order.getCrossID());
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertNotNull(report);
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals("VND", report.getSymbol());
		Assert.assertEquals(ExecType.CANCEL, report.getExecType());
		Assert.assertEquals(OrdStatus.CANCELORREPLACE, report.getOrdStatus());
		Assert.assertEquals(report.getOrigClOrdID(), report.getOrderID());
		Assert.assertEquals("CANCEL", order.getCurrentStatus());
	}

	@Test
	public void testSendAckIf2FirmOrder() {
		NewOrderCross order = create2FirmOrder();
		orderController.onEvent(order);
		orderController.ackTwoFirmOrder(order.getCrossID());
		NewOrderCross report = (NewOrderCross) responses.get(0);
		Assert.assertEquals(order.getSenderCompID(), report.getTargetCompID());
		Assert.assertEquals(order.getCrossID(), report.getCrossID());
		Assert.assertEquals(order.getCrossType(), report.getCrossType());
		Assert.assertEquals(order.getCrossPrioritization(), report.getCrossPrioritization());
		Assert.assertEquals(order.getSettlType(), report.getSettlType());
		Assert.assertEquals(order.getGroupSides(), report.getGroupSides());
		Assert.assertEquals(order.getPrice(), report.getPrice(), 0);
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals("NEW", order.getCurrentStatus());
	}

	@Test
	public void testSendAcceptWhenBuyerAccept2FirmOrder() {
		NewOrderCross order = create2FirmOrder();
		orderController.onEvent(order);
		orderController.acceptTwoFirm(order.getCrossID());
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertEquals(order.getCrossID(), report.getOrderID());
		Assert.assertEquals(order.getCrossID(), report.getOrigClOrdID());
		Assert.assertEquals(order.getCrossID(), report.getSecondaryClOrdID());
		Assert.assertEquals(ExecType.FILL, report.getExecType());
		Assert.assertEquals(OrdStatus.FILL, report.getOrdStatus());
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals(order.getPrice(), report.getLastPx(), 0);
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals(order.getGroupSides().get(0).getOrderQty(), report.getLastQty(), 0);
		Assert.assertEquals("FILL", order.getCurrentStatus());
	}
	
	@Test
	public void testSendRejectFromBuyerWith2FirmOrder() {
		NewOrderCross order = create2FirmOrder();
		orderController.onEvent(order);
		orderController.rejectTwoFirm(order.getCrossID());
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertEquals(order.getSenderCompID(), report.getTargetCompID());
		verifyReject(order, report);
	}

	private void verifyReject(NewOrderCross order, ExecutionReport report) {
		Assert.assertEquals(order.getGroupSides().get(0).getAccount(), report.getAccount());
		Assert.assertEquals(order.getCrossID(), report.getOrderID());
		Assert.assertEquals(order.getCrossID(), report.getClOrdID());
		Assert.assertEquals(order.getCrossID(), report.getOrigClOrdID());
		Assert.assertEquals(ExecType.CANCEL, report.getExecType());
		Assert.assertEquals(OrdStatus.CANCELORREPLACE, report.getOrdStatus());
		Assert.assertEquals('P', report.getOrdType());
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals(order.getPrice(), report.getPrice(), 0);
		Assert.assertEquals(order.getGroupSides().get(0).getOrderQty(), report.getLeavesQty(), 0);
		Assert.assertEquals("CANCEL", order.getCurrentStatus());
	}

	@Test
	public void testSendAcceptCancelFromBuyer2Firm() {
		NewOrderCross order = create2FirmOrder();
		orderController.onEvent(order);
		orderController.acceptCancelFromBuyerTwoFirm(order.getCrossID());
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertEquals(order.getSenderCompID(), report.getTargetCompID());
		Assert.assertEquals(order.getGroupSides().get(0).getAccount(), report.getAccount());
		Assert.assertEquals(order.getCrossID(), report.getOrderID());
		Assert.assertEquals(order.getGroupSides().get(0).getClOrdID(), report.getClOrdID());
		Assert.assertEquals(order.getCrossID(), report.getOrigClOrdID());
		Assert.assertEquals(ExecType.CANCEL, report.getExecType());
		Assert.assertEquals(OrdStatus.PENDING_NEW, report.getOrdStatus());
		Assert.assertEquals('P', report.getOrdType());
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals(order.getPrice(), report.getPrice(), 0);
		Assert.assertEquals(order.getGroupSides().get(0).getOrderQty(), report.getLeavesQty(), 0);
		Assert.assertEquals("CANCEL WAIT FOR APPROVAL", order.getCurrentStatus());
	}

	@Test
	public void testSendAcceptCancelFromExchange2Firm() {
		NewOrderCross order = create2FirmOrder();
		orderController.onEvent(order);
		orderController.acceptCancelFromExchangeTwoFirm(order.getCrossID());
		ExecutionReport report = (ExecutionReport) responses.get(0);
		Assert.assertEquals(order.getSenderCompID(), report.getTargetCompID());
		Assert.assertEquals(order.getGroupSides().get(0).getAccount(), report.getAccount());
		Assert.assertEquals(order.getCrossID(), report.getOrderID());
		Assert.assertEquals(order.getGroupSides().get(0).getClOrdID(), report.getClOrdID());
		Assert.assertEquals(order.getCrossID(), report.getOrigClOrdID());
		Assert.assertEquals(ExecType.CANCEL, report.getExecType());
		Assert.assertEquals(OrdStatus.CANCELORREPLACE, report.getOrdStatus());
		Assert.assertEquals('P', report.getOrdType());
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals('8', report.getSide());
		Assert.assertEquals(order.getPrice(), report.getPrice(), 0);
		Assert.assertEquals(order.getGroupSides().get(0).getOrderQty(), report.getLeavesQty(), 0);
		Assert.assertEquals("CANCEL", order.getCurrentStatus());
	}
	
	private CrossOrderCancelRequest createCancelCrossOrder() {
		CrossOrderCancelRequest cancelOrder = new CrossOrderCancelRequest();
		cancelOrder.setSenderCompID("0211GW");
		cancelOrder.setCrossID("VND0000010001");
		cancelOrder.setOrigCrossID("VND0000010001");
		return cancelOrder;
	}
	
	private NewOrderCross create2FirmOrder() {
		NewOrderCross orderCross = createOneFirmOrder();
		GroupSide group = orderCross.getGroupSides().get(1);
		group.setClOrdID("");
		group.setPartyID("023");
		group.setAccount("");
		return orderCross;
	}

	private NewOrderCross create2FirmOrderFakedSeller() {
		NewOrderCross orderCross = createOneFirmOrder();
		GroupSide group0 = orderCross.getGroupSides().get(0);
		group0.setClOrdID("clid1");
		group0.setAccount("021C019385");
		group0.setPartyID("023");
		GroupSide group = orderCross.getGroupSides().get(1);
		orderCross.setSenderCompID(null);
		group.setClOrdID("clid2");
		group.setPartyID("021");
		group.setAccount("");

		return orderCross;
	}
	
	private NewOrderCross createOneFirmOrder() {
		NewOrderCross newOrder = new NewOrderCross();
		newOrder.setSenderCompID("0211GW");
		newOrder.setMsgSeqNum(1);
		newOrder.setCrossID("010001");
		newOrder.setCrossType("1");
		newOrder.setSettlType("3");
		GroupSide group1 = new GroupSide();
		group1.setSide('2');
		group1.setClOrdID("clid1");
		group1.setAccount("021C019385");
		group1.setOrderQty(50001);
		group1.setPartyID("021");
		group1.setAccountType("1");
		group1.setNoPartyIDs(1);
		newOrder.addGroup(group1);
		GroupSide group2 = new GroupSide();
		group2.setSide('1');
		group2.setClOrdID("clid2");
		group2.setAccount("021C019385");
		group2.setOrderQty(50001);
		group2.setPartyID("021");
		group2.setAccountType("1");
		group2.setNoPartyIDs(1);;
		newOrder.addGroup(group2);
		newOrder.setSymbol("VND");
		newOrder.setPrice(11100);
		return newOrder;
	}

	@Test
	public void testRejectCancelTwoFirmFromExchange() {
		NewOrderCross order = create2FirmOrder();
		orderController.onEvent(order);
		String rejectCode = "-11003";
		orderController.rejectCancelTwoFirmCrossOrder(order.getCrossID(), rejectCode);
		ExecutionReport report = (ExecutionReport) responses.get(0);
		verifyFill(order, report);
	}

	private void verifyFill(NewOrderCross order, ExecutionReport report) {
		Assert.assertEquals("FILL", order.getCurrentStatus());
		Assert.assertEquals("0211GW", report.getTargetCompID());
		Assert.assertEquals(order.getGroupSides().get(0).getAccount(), report.getAccount());
		Assert.assertEquals(order.getCrossID(), report.getOrderID());
		Assert.assertEquals(order.getGroupSides().get(0).getClOrdID(), report.getClOrdID());
		Assert.assertEquals(order.getCrossID(), report.getOrigClOrdID());
		Assert.assertEquals(ExecType.FILL, report.getExecType());
		Assert.assertEquals(OrdStatus.FILL, report.getOrdStatus());
		Assert.assertEquals('P', report.getOrdType());
		Assert.assertEquals(order.getSymbol(), report.getSymbol());
		Assert.assertEquals('8', report.getSide());
	}

	@Test
	public void testCreateCrossOrder2FirmWithFakedSellerShouldSendMessageToBuyer() {
		NewOrderCross order = create2FirmOrderFakedSeller();
		orderController.onEvent(order);
		NewOrderCross order2 = (NewOrderCross) responses.get(0);
		Assert.assertNotNull(order2);
		Assert.assertEquals(order.getGroupSides().get(1).getPartyID() + "1GW", order2.getTargetCompID());
		Assert.assertEquals("NEW", order.getCurrentStatus());
		Assert.assertEquals(2, order2.getGroupSides().size());
		Assert.assertEquals("021C019385", order2.getGroupSides().get(0).getAccount());
		Assert.assertEquals("clid1", order2.getGroupSides().get(0).getClOrdID());
		Assert.assertTrue('2' == order2.getGroupSides().get(0).getSide());
		Assert.assertEquals(50001, order2.getGroupSides().get(0).getOrderQty(), 0);
		Assert.assertEquals("023", order2.getGroupSides().get(0).getPartyID());
		Assert.assertEquals(1, order2.getGroupSides().get(0).getNoPartyIDs(), 0);
		Assert.assertEquals("", order2.getGroupSides().get(1).getAccount());
		Assert.assertEquals("", order2.getGroupSides().get(1).getClOrdID());
		Assert.assertTrue('1' == order2.getGroupSides().get(1).getSide());
		Assert.assertEquals(50001, order2.getGroupSides().get(1).getOrderQty(), 0);
		Assert.assertEquals("021", order2.getGroupSides().get(1).getPartyID());
		Assert.assertEquals(1, order2.getGroupSides().get(1).getNoPartyIDs(), 0);
	}
	
	@Test
	public void testCrossOrderOfFakedSellerReceiveConfirmFromBuyer() {
		NewOrderCross order = create2FirmOrderFakedSeller();
		orderController.onEvent(order);
		
		NewOrderCross orderConfirm = create2FirmOrderFakedSeller();
		orderConfirm.setCrossType("5");
		orderConfirm.setCrossID(order.getCrossID());
		orderController.onEvent(orderConfirm);
		
		ExecutionReport report = (ExecutionReport) responses.get(1);
		Assert.assertNotNull(report);
		verifyFill(orderConfirm, report);
	}
	
	@Test
	public void testCrossOrderOfFakedSellerReceiveRejectFromBuyer() {
		NewOrderCross order = create2FirmOrderFakedSeller();
		orderController.onEvent(order);

		NewOrderCross orderConfirm = create2FirmOrderFakedSeller();
		orderConfirm.setCrossType("6");
		orderConfirm.setCrossID(order.getCrossID());
		orderController.onEvent(orderConfirm);
		
		ExecutionReport report = (ExecutionReport) responses.get(1);
		Assert.assertNotNull(report);
		verifyReject(orderConfirm, report);
	}

	@Test
	public void testCrossOrderOfFakedSellerSendCancelToBuyerShouldCreateCancelAckMessage() {
		NewOrderCross order = create2FirmOrderFakedSeller();
		orderController.onEvent(order);
		NewOrderCross orderConfirm = create2FirmOrderFakedSeller();
		orderConfirm.setCrossID(order.getCrossID());
		orderConfirm.setCrossType("5");
		orderController.onEvent(orderConfirm);
		orderController.sendCancelAckWithCrossID(order.getCrossID());
		CrossOrderCancelRequest cancelOrderResponse = (CrossOrderCancelRequest) responses.get(2);
		verifyCancelAck(orderConfirm, cancelOrderResponse);
	}

	@Test
	public void testCrossOrderOfFakedSellerReceiveAcceptCancelFromBuyer() {
		NewOrderCross order = create2FirmOrderFakedSeller();
		orderController.onEvent(order);
		CrossOrderCancelRequest cancelOrder = createCancelCrossOrder();
		cancelOrder.setCrossType("5");
		orderController.onEvent(cancelOrder);
		Assert.assertEquals("CANCEL WAIT FOR APPROVAL", order.getCurrentStatus());
	}

	@Test
	public void testCrossOrderOfFakedSellerReceiveRejectCancelFromBuyer() {
		NewOrderCross order = create2FirmOrderFakedSeller();
		orderController.onEvent(order);
		CrossOrderCancelRequest cancelOrder = createCancelCrossOrder();
		cancelOrder.setCrossType("6");
		orderController.onEvent(cancelOrder);
		Assert.assertEquals("REJECT CANCEL WAIT FOR APPROVAL", order.getCurrentStatus());
	}
	
	@Test
	public void testCall() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method[] methods = orderController.getClass().getDeclaredMethods();
		for(Method method : methods) {
			if (method.getName().equals("processAction")) {
				method.invoke(orderController, "rejectByExchangeWithCode", "a,b");
			}
		}
	}
}
