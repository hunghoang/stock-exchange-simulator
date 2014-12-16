package vn.com.vndirect.exchangesimulator.matching;

import java.util.LinkedList;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

class RFCondSessionAllOrderMatcher extends ContinuousSessionAllOrderMatcher {
	@Override
	protected int getFloor(String symbol) {
		return 14000;
	}

	@Override
	protected int getCeil(String symbol) {
		return 50000;
	}

}

public class RFKeywords {
	private ContinuousSessionAllOrderMatcher matcher;
	private LinkedList<ExecutionReport> queueExecutionReports;

	public RFKeywords() {
		matcher = new RFCondSessionAllOrderMatcher();
	}

	public void pushLOOrder(String orderID, String account, String side, String symbol, int quantity, int price) {
		NewOrderSingle order = new NewOrderSingle();
		order.setOrderId(orderID);
		if ("Buy".equalsIgnoreCase(side)) {
			order.setSide(NewOrderSingle.BUY);
		} else {
			order.setSide(NewOrderSingle.SELL);
		}
		order.setOrdType('2');
		order.setAccount(account);
		order.setSymbol(symbol);
		order.setOrderQty(quantity);
		order.setPrice((double) price);

		matcher.push(order);
		queueExecutionReports = new LinkedList<ExecutionReport>(matcher.getExecutionReport(symbol));
	}

	public String getMatchingResult() {
		if (queueExecutionReports.size() == 0) {
			return "";
		}
		ExecutionReport executionReport1 = queueExecutionReports.pop();
		ExecutionReport executionReport2 = queueExecutionReports.pop();
		return convertToString(executionReport1) + ";" + convertToString(executionReport2);
	}

	private String convertToString(ExecutionReport executionReport) {
		return executionReport.getOrderID() + "," + executionReport.getAccount() + "," + executionReport.getSymbol() + "," + executionReport.getOrderQty()
				+ "," + Math.round(executionReport.getPrice()) + "," + executionReport.getOrdStatus();
	}

	public void resetMatcher() {
		matcher.clear();
	}
}
