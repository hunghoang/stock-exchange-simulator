package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class SymbolMatcherTest {
	private ContinuousSessionMatcher sm;
	private OrderPriceIndex orderPriceIndex;


	@Before
	public void setUp() {
		orderPriceIndex = new OrderPriceIndex();
		sm = new ContinuousSessionMatcher("VND", new PriceRange(9000, 16300, 100), new OrderMatcher(new ContinuousReport()), orderPriceIndex);
	}

	@Test
	public void testPushSingleOrder() {
		NewOrderSingle order = OrderFactory.createLOBuy(1000, 16000);
		sm.push(order);
		OrderList list = sm.getOrderList(16000);
		Assert.assertEquals(order, list.get(0));
	}

	@Test
	public void testMatchFullSingleOrder() {
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		sm.push(OrderFactory.createLOSell(1000, 16000));
		List<ExecutionReport> rps = sm.getLastMatches();
		Assert.assertEquals(2, rps.size());
	}

	public void testMatchPartialSingleOrder() {
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		sm.push(OrderFactory.createLOSell(500, 16000));
		List<ExecutionReport> rps = sm.getLastMatches();
		Assert.assertEquals(2, rps.size());
	}

	@Test
	public void testPush2BuyOrder() {
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		List<ExecutionReport> rps = sm.getLastMatches();
		Assert.assertEquals(0, rps.size());
	}
	
	@Test
	public void matchMultipleOrderInTheSameSlot(){
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		sm.push(OrderFactory.createLOBuy(1000, 16100));
		sm.push(OrderFactory.createLOSell(2500, 16000));
		List<ExecutionReport> rps = sm.getLastMatches();
		Assert.assertEquals(6, rps.size());
	}

	@Test
	public void testMatchMultipleOrder() {
		NewOrderSingle sellOrderSingle  = OrderFactory.createLOSell(1500, 16000);
		sellOrderSingle.setSenderCompID("2");
		NewOrderSingle buyOrderSingle1 = OrderFactory.createLOBuy(1000, 16000);
		buyOrderSingle1.setSenderCompID("1");
		NewOrderSingle buyOrderSingle2 = OrderFactory.createLOBuy(1000, 16100);
		buyOrderSingle2.setSenderCompID("3");
		
		sm.push(buyOrderSingle1);
		sm.push(buyOrderSingle2);
		sm.push(sellOrderSingle);
		List<ExecutionReport> rps = sm.getLastMatches();
		Assert.assertEquals(4, rps.size());
	}
	
	@Test
	public void testMatchPartialSingleOrderAndCheckBestPrice() {
		sm.push(OrderFactory.createLOSell(300, 10000));
		sm.push(OrderFactory.createLOBuy(500, 10200));
		List<ExecutionReport> rps = sm.getLastMatches();
		Assert.assertEquals(2, rps.size());
		Assert.assertEquals(10000, rps.get(0).getLastPx(), 0);
		
	}

}
