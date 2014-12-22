package vn.com.vndirect.exchangesimulator.matching;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class MatchDetectionTest {

	private ContinuousSessionMatcher sm;

	private RangeMatcher matcher;
	
	private NewOrderSingle createBuy(int quantity, int price) {
		return OrderFactory.createLOOrder(String.valueOf(Math.random()), NewOrderSingle.BUY,
				"VND", quantity, price);
	}

	private NewOrderSingle createSell(int quantity, int price) {
		return OrderFactory.createLOOrder(String.valueOf(Math.random()), NewOrderSingle.SELL,
				"VND", quantity, price);
	}

	@Before
	public void setUp() {
		OrderMatcher orderMatcher = new OrderMatcher(new ContinuousReport());
		sm = new ContinuousSessionMatcher("VND", new PriceRange(16000, 16200, 100), orderMatcher, new OrderPriceIndex());
		matcher = new LORangeMatcher(new PriceRange(16000, 16200, 100), sm.getOrders(), new OrderPriceIndex(), orderMatcher);
	}

	@Test
	public void testRangeForBuyOrder() {
		sm.push(createBuy(1000, 16000));
		sm.push(createBuy(1000, 16100));
		Assert.assertArrayEquals(new int[] { 1, 0, -1 },
				matcher.getMatchingRange(createSell(1500, 16000)));
	}

	@Test
	public void testRangeForSellOrder() {
		sm.push(createSell(1000, 16000));
		sm.push(createSell(1000, 16100));
		Assert.assertArrayEquals(new int[] { 0, 2, 1 },
				matcher.getMatchingRange(createBuy(1500, 16200)));
	}

}
