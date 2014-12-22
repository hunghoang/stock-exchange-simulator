package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ContinuousSessionMatcherTest {

	
	@Test
	public void testDoNotMatchAfterOrderIsCanceled() {
		ContinuousSessionMatcher matcher = new ContinuousSessionMatcher("VND", new PriceRange(15000, 17000, 100), new OrderMatcher(new ContinuousReport()), new OrderPriceIndex());
		NewOrderSingle order1 = OrderFactory.createLOBuy("VND", 1000, 16000);
		NewOrderSingle order2 = OrderFactory.createLOSell("VND", 1000, 16000);		
		matcher.push(order1);
		order1.setOrderQty(0);
		matcher.push(order2);
		List<ExecutionReport> result = matcher.getLastMatches();
		Assert.assertEquals(0, result.size());
	}
	
	
	
}
