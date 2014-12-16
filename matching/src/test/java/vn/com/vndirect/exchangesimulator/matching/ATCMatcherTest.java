package vn.com.vndirect.exchangesimulator.matching;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCMatcherTest {

	PriceRange range;
	String symbol;
	ATCBestMatches matcher;

	@Before
	public void setup() {
		range = new PriceRange(16000, 16500, 100);
		symbol = "VND";
		matcher = new ATCBestMatches(range, symbol);
	}

	@Test
	public void testPutATCOrder() {
		NewOrderSingle ATCOrder = OrderFactory
				.createATCBuy(symbol, 1000);
		NewOrderSingle buyOrder = OrderFactory.createLOBuy(symbol, 1000, 16000);
		matcher.push(ATCOrder);
		matcher.push(buyOrder);
		Assert.assertEquals(2, matcher.getBestOrders(NewOrderSingle.BUY).size());
		Assert.assertEquals(ATCOrder, matcher.getBestOrders(NewOrderSingle.BUY).get(0));
		Assert.assertEquals(buyOrder, matcher.getBestOrders(NewOrderSingle.BUY).get(1));
	}

	@Test
	public void testPutSellOrderWithATC() {
		NewOrderSingle ATCOrder = OrderFactory.createATCSell(symbol, 1000);
		NewOrderSingle o1 = OrderFactory.createLOSell(symbol, 1000, 16200);
		NewOrderSingle o2 = OrderFactory.createLOSell(symbol, 1000, 16000);
		matcher.push(ATCOrder);
		matcher.push(o1);
		matcher.push(o2);
		Assert.assertEquals(ATCOrder, matcher.getBestOrders(NewOrderSingle.SELL).get(0));
		Assert.assertEquals(o2, matcher.getBestOrders(NewOrderSingle.SELL).get(1));
	}

	@Test
	public void testSimpleATCMatch() {
		ATCSessionMatcher matcher = new ATCSessionMatcher(new PriceRange(16000,
				16900, 100), "VND");
		matcher.push(OrderFactory.createATCBuy("VND", 2000));
		matcher.push(OrderFactory.createLOBuy("VND", 4500, 16500));
		matcher.push(OrderFactory.createATCSell("VND", 1000));
		matcher.push(OrderFactory.createLOSell("VND", 500, 16700));
		matcher.processATC();
		Assert.assertEquals(4, matcher.getMatchedResult().size());
		Assert.assertEquals(2, matcher.getExpiredResult().size());
	}

	@Test
	public void testOnlyATC(){
		ATCSessionMatcher matcher = new ATCSessionMatcher(new PriceRange(16000,
				16900, 100), "VND");
		matcher.push(OrderFactory.createATCBuy("VND", 500));
		matcher.push(OrderFactory.createATCSell("VND", 3000));
		matcher.processATC();
	}
	
	@Test
	public void testOnlyMatchATCOrder() {
		ATCSessionMatcher matcher = new ATCSessionMatcher(new PriceRange(16000,
				16900, 100), "VND");
		matcher.push(OrderFactory.createATCBuy("VND", 1000));
		matcher.push(OrderFactory.createLOBuy("VND", 4500, 16500));
		matcher.push(OrderFactory.createATCSell("VND", 1000));
		matcher.push(OrderFactory.createLOSell("VND", 500, 16700));
		matcher.processATC();
		Assert.assertEquals(2, matcher.getMatchedResult().size());
		Assert.assertEquals(2, matcher.getExpiredResult().size());
	}
}
