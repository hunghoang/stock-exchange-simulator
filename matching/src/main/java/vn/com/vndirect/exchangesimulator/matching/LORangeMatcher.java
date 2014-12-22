package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class LORangeMatcher extends RangeMatcher {

	private OrderPriceIndex orderPriceIndex;
	private PriceRange priceRange;

	public LORangeMatcher(PriceRange priceRange, OrderList[] orders, OrderPriceIndex orderPriceIndex, OrderMatcher orderMatcher) {
		this.priceRange = priceRange;
		this.orders = orders;
		this.orderPriceIndex = orderPriceIndex;
		this.matcher = orderMatcher;
	}
	
	@Override
	protected int[] getMatchingRange(NewOrderSingle order) {
		int start = 0, end = -1, direction = 0;
		int index = priceRange.priceToIndex(order.getPrice());
		if (order.getSide() == NewOrderSingle.BUY) {
			start = orderPriceIndex.getBestSellIndex(orders);
			end = index;
			direction = 1;
		}
		
		if (order.getSide() == NewOrderSingle.SELL) {
			start = orderPriceIndex.getBestBuyIndex(orders);
			end = index;
			direction = -1;
		}
		return new int[] { start, end, direction };
	}

	@Override
	protected boolean hasNoMatch(NewOrderSingle order) {
		int index = priceRange.priceToIndex(order.getPrice());
		int bestBuyIndex = orderPriceIndex.getBestBuyIndex(orders);
		int bestSellIndex = orderPriceIndex.getBestSellIndex(orders);
		if (bestBuyIndex == -1 && bestSellIndex == orders.length) {
			return true;
		}

		if (order.getSide() == NewOrderSingle.BUY && bestSellIndex > index) {
			return true;
		}

		if (order.getSide() == NewOrderSingle.SELL && bestBuyIndex < index) {
			return true;
		}
		return false;
	}

	@Override
	protected List<ExecutionReport> processOrderIfNoMatch(NewOrderSingle order) {
		int index = priceRange.priceToIndex(order.getPrice());
		orders[index].add(order);
		return new ArrayList<ExecutionReport>();
	}

	@Override
	protected void processOrderAfterMatching(NewOrderSingle order, List<ExecutionReport> reports) {
		int index = priceRange.priceToIndex(order.getPrice());
		if (order.getOrderQty() > 0) {
			orders[index].add(order);
		}
	}

}
