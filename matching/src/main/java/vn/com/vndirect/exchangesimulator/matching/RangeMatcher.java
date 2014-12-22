package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public abstract class RangeMatcher {
	
	protected OrderMatcher matcher;

	protected OrderList[] orders;

	public List<ExecutionReport> match(NewOrderSingle order) {
		List<ExecutionReport> reports = new ArrayList<ExecutionReport>();
		if (hasNoMatch(order)) {
			reports.addAll(processOrderIfNoMatch(order));
			return reports;
		}
		
		reports.addAll(rangeMatching(order));
		
		processOrderAfterMatching(order, reports);
		return reports;
	}
	
	protected abstract void processOrderAfterMatching(NewOrderSingle order, List<ExecutionReport> reports);

	protected abstract List<ExecutionReport> processOrderIfNoMatch(NewOrderSingle order);

	protected abstract boolean hasNoMatch(NewOrderSingle order);


	protected List<ExecutionReport> rangeMatching(NewOrderSingle order) {
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
	
	protected abstract int[] getMatchingRange(NewOrderSingle order);

	public void setMatcher(OrderMatcher matcher) {
		this.matcher = matcher;
	}

	public void setOrders(OrderList[] orders) {
		this.orders = orders;
	}


}
