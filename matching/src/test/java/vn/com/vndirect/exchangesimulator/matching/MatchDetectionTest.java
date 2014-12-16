package vn.com.vndirect.exchangesimulator.matching;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class MatchDetectionTest {

	private ContinuousSessionMatcher sm;

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
		sm = new ContinuousSessionMatcher("VND", 16000, 16200, 100);
	}

	@Test
	public void bestBuyPriceUpdatedAfterFirstBuyOrder() {
		sm.push(createBuy(1000, 16200));
		Assert.assertEquals(16200, sm.getBestBuyPrice());
	}

	@Test
	public void bestSellPriceInMultipleOrder() {
		sm.push(createSell(1000, 16000));
		sm.push(createSell(1000, 16100));
		Assert.assertEquals(16000, sm.getBestSellPrice());
	}

	@Test
	public void bestBuyPriceNullIfNoMoreorderToMatch() {
		NewOrderSingle buyOrderSingle = createBuy(1000, 16200);
		buyOrderSingle.setSenderCompID("1");
		NewOrderSingle sellOrderSingle = createSell(1000, 16200);
		sellOrderSingle.setSenderCompID("2");
		sm.push(buyOrderSingle);
		sm.push(sellOrderSingle);
		Assert.assertEquals(-1, sm.getBestBuyPrice());
	}

	@Test
	public void testRangeForBuyOrder() {
		sm.push(createBuy(1000, 16000));
		sm.push(createBuy(1000, 16100));
		Assert.assertArrayEquals(new int[] { 1, 0, -1 },
				sm.getMatchingRange(createSell(1500, 16000)));
	}

	@Test
	public void testRangeForSellOrder() {
		sm.push(createSell(1000, 16000));
		sm.push(createSell(1000, 16100));
		Assert.assertArrayEquals(new int[] { 0, 2, 1 },
				sm.getMatchingRange(createBuy(1500, 16200)));
	}

}
