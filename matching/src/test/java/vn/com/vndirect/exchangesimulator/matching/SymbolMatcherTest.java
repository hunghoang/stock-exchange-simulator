package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class SymbolMatcherTest {
	private ContinuousSessionMatcher sm;


	@Before
	public void setUp() {
		sm = new ContinuousSessionMatcher("VND", 16000, 16300, 100);
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
	public void remainingUnfilledOrderPushIntoItsPriceSlot() {
		NewOrderSingle sellOrderSingle  = OrderFactory.createLOSell(500, 16000);
		sellOrderSingle.setSenderCompID("2");
		NewOrderSingle buyOrderSingle = OrderFactory.createLOBuy(1000, 16200);
		buyOrderSingle.setSenderCompID("1");
		sm.push(sellOrderSingle);
		sm.push(buyOrderSingle);
		Assert.assertEquals(16200, sm.getBestBuyPrice());
	}

	@Test
	public void testGetBestBuyPrice() {
		sm.push(OrderFactory.createLOBuy(1500, 16300));
		sm.push(OrderFactory.createLOBuy(1000, 16200));
		sm.push(OrderFactory.createLOBuy(2000, 16100));
		Assert.assertEquals(16300, sm.getBestBuyPrice());
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
	public void testMatchBetterBuyPrice() {
		sm.push(OrderFactory.createLOBuy(1000, 16000));
		sm.push(OrderFactory.createLOBuy(1000, 16100));
		sm.push(OrderFactory.createLOSell(1500, 16000));
		Assert.assertEquals(16000, sm.getBestBuyPrice());
	}
}
