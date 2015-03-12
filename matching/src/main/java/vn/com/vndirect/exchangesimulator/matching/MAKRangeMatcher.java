package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class MAKRangeMatcher extends MTLRangeMatcher {

	public MAKRangeMatcher(PriceRange priceRange, OrderList[] orders, OrderPriceIndex orderPriceIndex, OrderMatcher orderMatcher) {
		super(priceRange, orders, orderPriceIndex, orderMatcher);
	}
	
	@Override
	protected void processOrderAfterMatching(NewOrderSingle order, List<ExecutionReport> reports) {
		if (order.getOrderQty() > 0) {
			reports.add(generateRejectReport(order));
		}
	}
	
	private ExecutionReport generateRejectReport(NewOrderSingle order) {
		return processOrderIfNoMatch(order).get(0);
	}




}
