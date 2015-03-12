package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class MOKRangeMatcher extends MAKRangeMatcher {

	private static final Logger log = Logger.getLogger(MOKRangeMatcher.class);
	

	public MOKRangeMatcher(PriceRange priceRange, OrderList[] orders, OrderPriceIndex orderPriceIndex, OrderMatcher orderMatcher) {
		super(priceRange, orders, orderPriceIndex, orderMatcher);
	}
	
	@Override
	protected List<ExecutionReport> rangeMatching(NewOrderSingle order) {
		int[] range = getMatchingRange(order);
		int start = range[0];
		int end = range[1];
		int direction = range[2];
		List<ExecutionReport> tempReports = new ArrayList<>();
		int lastIndex = start;

		OrderList list = new OrderList();
		if (start == end) {
			list.addAll(orders[start]);
		} else {
			do {
				list.addAll(orders[lastIndex]);
				lastIndex += direction;
			} while (lastIndex != end + direction);
		}

		if (isMatchAll(order, list)) {
			return matcher.match(order, list);
		}
		
		return tempReports;
	}
	
	private boolean isMatchAll(NewOrderSingle order, OrderList orderList) {
		int accumulateQty = 0;
		for (NewOrderSingle orderElement : orderList.items()) {
			accumulateQty += orderElement.getOrderQty();
		}
		return accumulateQty >= order.getOrderQty();
	}

}
