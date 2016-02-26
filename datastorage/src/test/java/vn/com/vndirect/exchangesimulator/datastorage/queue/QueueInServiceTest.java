package vn.com.vndirect.exchangesimulator.datastorage.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.Logon;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;

public class QueueInServiceTest {

	private QueueInServiceImpl service;
	private OrderQueue orderQueue;
	private LogonQueue logonQueue;
	private TradingSessionStatusRequestQueue tradingSessionStatusRequestQueue;
	private SecurityStatusRequestQueue securityStatusRequestQueue;
	private OthersQueue othersQueue;

	@Before
	public void setUp() {
		service = new QueueInServiceImpl();

		orderQueue = new OrderQueue();
		logonQueue = new LogonQueue();
		tradingSessionStatusRequestQueue = new TradingSessionStatusRequestQueue();
		securityStatusRequestQueue = new SecurityStatusRequestQueue();

		othersQueue = new OthersQueue();
		service.setOthersQueue(othersQueue);
		service.setOrderQueue(orderQueue);
	}

	@Test
	public void testRouteTradingSessionStatusRequestQueue() throws InterruptedException {
		TradingSessionStatusRequest request = new TradingSessionStatusRequest();
		request.setBeginString("asdasdas");
		request.setSendingTime("aasd2");

		Object objToAdd = "asdsadasd";

		service.setTradingSessionStatusRequestQueue(tradingSessionStatusRequestQueue);

		service.add(objToAdd);
		service.add(request);

		Thread.sleep(200);

		Assert.assertEquals(0, service.size());
		Assert.assertEquals(1, tradingSessionStatusRequestQueue.size());
		Assert.assertEquals(request, tradingSessionStatusRequestQueue.peek());
		Assert.assertEquals(objToAdd, othersQueue.peek());
	}

	@Test
	public void testRouteSecurityStatusRequestQueue() throws InterruptedException {
		SecurityStatusRequest request = new SecurityStatusRequest();
		request.setBeginString("sadsdsad");
		request.setSecurityStatusReqID("asdasdasdasd");

		Object objToAdd = "asdsadasd";

		service.setSecurityStatusRequestQueue(securityStatusRequestQueue);

		service.add(objToAdd);
		service.add(request);
		Thread.sleep(200);

		Assert.assertEquals(0, service.size());
		Assert.assertEquals(1, securityStatusRequestQueue.size());
		Assert.assertEquals(request, securityStatusRequestQueue.peek());
		Assert.assertEquals(objToAdd, othersQueue.peek());
	}

	@Test
	public void testRouteOrderQueue() throws InterruptedException {
		NewOrderSingle orderSingle = new NewOrderSingle();
		orderSingle.setAccount("xxx");
		orderSingle.setSymbol("aaa");

		Object objToAdd = "asdsadasd";

		service.add(objToAdd);
		service.add(orderSingle);

		Thread.sleep(200);
		Assert.assertEquals(0, service.size());
		Assert.assertEquals(1, orderQueue.size());
		Assert.assertEquals(orderSingle, orderQueue.peek());
		Assert.assertEquals(objToAdd, othersQueue.peek());
	}

	@Test
	public void testRouteOrderCancelRequestQueue() throws InterruptedException {
		OrderCancelRequest request = new OrderCancelRequest();
		request.setSymbol("AAA");
		request.setOrigClOrdID("1234567890");
		request.setTransactTime(new Date(1234567890));

		String aString = "abcxyz";

		service.add(aString);
		service.add(request);

		Thread.sleep(200);
		assertEquals(0, service.size());
		assertEquals(1, orderQueue.size());
		assertEquals(request, orderQueue.peek());
	}

	@Test
	public void testRouteOrderReplaceRequestQueue() throws InterruptedException {
		OrderReplaceRequest request = new OrderReplaceRequest();
		request.setSymbol("ABC");
		request.setOrigClOrdID("1234567890");
		request.setTransactTime(new Date(1234567890));

		String aString = "abcxyz";

		service.add(aString);
		service.add(request);

		Thread.sleep(200);

		assertEquals(0, service.size());
		assertEquals(1, orderQueue.size());
		assertEquals(request, orderQueue.peek());
	}

	@Test
	public void testRouteLogonQueue() throws InterruptedException {

		Logon logon = new Logon();
		logon.setBeginString("asdasd");
		logon.setCheckSum("asdasdasdsad");

		Object objToAdd = "asdsadasd";

		service.setLogonQueue(logonQueue);

		service.add(objToAdd);
		service.add(logon);

		Thread.sleep(200);

		Assert.assertEquals(0, service.size());
		Assert.assertEquals(1, logonQueue.size());
		Assert.assertEquals(logon, logonQueue.peek());
		Assert.assertEquals(objToAdd, othersQueue.peek());
	}

	@Test
	public void testPollFromAnEmptyQueue() {
		assertNull(service.poll());
	}

	@Test
	public void testPeekEmpty() {
		assertNull(service.peek());
	}

}
