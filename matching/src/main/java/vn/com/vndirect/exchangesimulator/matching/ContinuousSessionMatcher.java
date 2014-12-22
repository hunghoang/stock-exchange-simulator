package vn.com.vndirect.exchangesimulator.matching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ContinuousSessionMatcher {
	
	private static final Logger log = Logger.getLogger(ContinuousSessionMatcher.class);
	
	private OrderList[] orders;
	private List<ExecutionReport> lastReports;
	private PriceRange priceRange;
	
	private Map<Character,RangeMatcher> rangeMatcherMap = new HashMap<>(); 

	
	public ContinuousSessionMatcher(String symbol, PriceRange priceRange, OrderMatcher orderMatcher, OrderPriceIndex orderPriceIndex) {
		this.priceRange = priceRange;
		orders = OrderList.createList(symbol, priceRange.getStepCount());
		rangeMatcherMap.put('2', new LORangeMatcher(priceRange, orders, orderPriceIndex, orderMatcher));
		rangeMatcherMap.put('T', new MTLRangeMatcher(priceRange, orders, orderPriceIndex, orderMatcher));
	}

	public void registMatcher(Character ordType, RangeMatcher matcher) {
		rangeMatcherMap.put(ordType, matcher);
	}
	
	public void push(NewOrderSingle order) {
		cleanOrderList(); // in case order is cancelled outside
		RangeMatcher rangeMatcher = getRangeMatcherByOrdType(order.getOrdType());
		
		if (rangeMatcher == null) {
			log.warn("No matcher for this order type: " + order.getOrdType());
		}
		
		lastReports = rangeMatcher.match(order);
	}
	
	private RangeMatcher getRangeMatcherByOrdType(char ordType) {
		return rangeMatcherMap.get(ordType);
	}

	private void cleanOrderList() {
		for (OrderList orderList : orders) {
			orderList.clearEmptyOrder();
		}
	}

	public OrderList getOrderList(int price) {
		return orders[priceRange.priceToIndex(price)];
	}

	public List<ExecutionReport> getLastMatches() {
		return lastReports;
	}
	
	protected OrderList[] getOrders() {
		return orders;
	}

}
