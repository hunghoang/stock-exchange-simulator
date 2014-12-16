package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ExpireReporter {

	public List<ExecutionReport> generateReport(OrderList orders) {
		List<ExecutionReport> result = new ArrayList<ExecutionReport>();
		for (NewOrderSingle order : orders.getList()) {
			if (order.getOrderQty() > 0) {
				result.add(generateReport(order));
			}
		}
		return result;
	}

	private ExecutionReport generateReport(NewOrderSingle order) {
		ExecutionReport report = new ExecutionReport();
		report.setTargetCompID(order.getSenderCompID());
		report.setOrdStatus('C');
		report.setOrderID(order.getOrderId());
		return report;
	}

}
