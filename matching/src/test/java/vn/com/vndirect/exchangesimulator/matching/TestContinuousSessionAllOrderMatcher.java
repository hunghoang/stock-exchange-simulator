package vn.com.vndirect.exchangesimulator.matching;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestContinuousSessionAllOrderMatcher {

	ContinuousSessionAllOrderMatcher continuousSessionAllOrderMatcher = new ContinuousSessionAllOrderMatcher() {
		protected int getCeil(String symbol) {
			return 50000;
		};
		protected int getFloor(String symbol) {
			return 10000;
		};
	};
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testMatchingOrder() {
		continuousSessionAllOrderMatcher.push(OrderFactory.createLOBuy("VND", 100, 16000));
		continuousSessionAllOrderMatcher.push(OrderFactory.createLOSell("VND", 100, 16000));
		Assert.assertEquals(2, continuousSessionAllOrderMatcher.getExecutionReport("VND").size());
	}
}
