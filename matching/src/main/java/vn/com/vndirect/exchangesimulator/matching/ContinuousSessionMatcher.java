package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ContinuousSessionMatcher {
	private OrderList[] orders;
	private List<ExecutionReport> lastReports;
	private OrderMatcher matcher;
	private PriceRange priceRange;

	public ContinuousSessionMatcher(String symbol, int floorPrice, int ceilingPrice, int priceStep) {
		this.matcher = new OrderMatcher(new ContinuousReport());
		this.priceRange = new PriceRange(floorPrice, ceilingPrice, priceStep);
		orders = OrderList.createList(symbol, priceRange.getStepCount());
	}

	private boolean hasNoMatch(NewOrderSingle order) {
		int index = priceRange.priceToIndex(order.getPrice());
		int bestBuyIndex = getBestBuyIndex();
		int bestSellIndex = getBestSellIndex();
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

	public int getBestBuyIndex() {
		for (int i = orders.length - 1; i >= 0; i--) {
			if (orders[i].size() > 0 && orders[i].get(0).getSide() == NewOrderSingle.BUY) {
				return i;
			}
		}
		return -1;
	}

	public int getBestSellIndex() {
		for (int i = 0; i < orders.length; i++) {
			if (orders[i].size() > 0 && orders[i].get(0).getSide() == NewOrderSingle.SELL) {
				return i;
			}
		}
		return orders.length;
	}

	private void cleanOrderList() {
		for (OrderList orderList : orders) {
			orderList.clearEmptyOrder();
		}
	}

	public int[] getMatchingRange(NewOrderSingle order) {
		int index = priceRange.priceToIndex(order.getPrice());
		int start = 0, end = -1, direction = 0;
		if (order.getSide() == NewOrderSingle.BUY) {
			start = getBestSellIndex();
			end = index;
			direction = 1;
		}

		if (order.getSide() == NewOrderSingle.SELL) {
			start = getBestBuyIndex();
			end = index;
			direction = -1;
		}
		return new int[] { start, end, direction };
	}

	public List<ExecutionReport> rangeMatching(NewOrderSingle order) {
		int[] range = getMatchingRange(order);
		int start = range[0];
		int end = range[1];
		int direction = range[2];
		List<ExecutionReport> tempReports = new ArrayList<>();
		int lastIndex = start;

		if (start == end) {
			return matcher.match(order, orders[start]);
		}

		do {
			tempReports.addAll(matcher.match(order, orders[lastIndex]));
			if (order.getOrderQty() == 0) {
				break;
			}
			lastIndex += direction;
		} while (lastIndex != end + direction);

		return tempReports;
	}

	public void push(NewOrderSingle order) {
		cleanOrderList(); // in case order is cancelled outside
		int index = priceRange.priceToIndex(order.getPrice());
		int bestBuyIndex = getBestBuyIndex();
		if (hasNoMatch(order)) {
			orders[index].add(order);
			lastReports = new ArrayList<>();
			if (order.getSide() == NewOrderSingle.BUY && index > bestBuyIndex) {
				bestBuyIndex = index;
			}
		} else {
			lastReports = rangeMatching(order);
			if (order.getOrderQty() > 0) {
				orders[index].add(order);
			}
			cleanOrderList();
		}
	}

	public OrderList getOrderList(int price) {
		return orders[priceRange.priceToIndex(price)];
	}

	public int getBestBuyPrice() {
		int bestBuyIndex = getBestBuyIndex();
		if (bestBuyIndex == -1) {
			return -1;
		}
		return priceRange.indexToPrice(bestBuyIndex);
	}

	public int getBestSellPrice() {
		int bestSellIndex = getBestSellIndex();
		if (bestSellIndex == -1) {
			return -1;
		}
		return priceRange.indexToPrice(bestSellIndex);
	}

	public List<ExecutionReport> getLastMatches() {
		return lastReports;
	}
}
