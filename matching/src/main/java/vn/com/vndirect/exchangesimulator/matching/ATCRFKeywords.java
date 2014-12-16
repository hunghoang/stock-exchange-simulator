package vn.com.vndirect.exchangesimulator.matching;

import java.util.LinkedList;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCRFKeywords {
	private ATCSessionMatcher matcher;
	private LinkedList<ExecutionReport> queueExecutionReports = new LinkedList<>();
	int floor = 15000;
	int ceil = 20000;

	public ATCRFKeywords() {
		initMatcher();
	}
	
	public void initPriceRange(int floor, int ceil){
		this.floor = floor;
		this.ceil = ceil;
	}
	
	public void initMatcher(){
		PriceRange range = new PriceRange(floor, ceil, 100);
		matcher = new ATCSessionMatcher(range, "AAA");
	}

	public void pushLOOrder(String orderID, String account, String side,
			String symbol, int quantity, int price) {
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
	}

	public void pushATCOrder(String orderID, String account, String side,
			String symbol, int quantity) {
		NewOrderSingle order = new NewOrderSingle();
		order.setOrderId(orderID);
		if ("Buy".equalsIgnoreCase(side)) {
			order.setSide(NewOrderSingle.BUY);
		} else {
			order.setSide(NewOrderSingle.SELL);
		}
		order.setOrdType('5');
		order.setAccount(account);
		order.setSymbol(symbol);
		order.setOrderQty(quantity);
		order.setPrice(0);
		matcher.push(order);
	}

	public void processATC() {
		try{
			matcher.processATC();
		}
		catch(NullPointerException ex){
			throw new NullPointerException("Exception when matching: " + ex.getStackTrace().toString());
		}

		queueExecutionReports = new LinkedList<ExecutionReport>(
				matcher.getMatchedResult());
	}

	public String getMatchingResult() {
		if (queueExecutionReports.size() == 0) {
			return "";
		}
		if (queueExecutionReports.size() < 2) {
			throw new RuntimeException("Reports must be even");
		}
		ExecutionReport executionReport1 = queueExecutionReports.pop();
		ExecutionReport executionReport2 = queueExecutionReports.pop();
		return convertToString(executionReport1) + ";"
				+ convertToString(executionReport2);
	}

	private String convertToString(ExecutionReport executionReport) {
		return executionReport.getOrderID() + ","
				+ executionReport.getAccount() + ","
				+ executionReport.getSymbol() + ","
				+ executionReport.getOrderQty() + ","
				+ executionReport.getPrice() + ","
				+ executionReport.getOrdStatus();
	}

	public void resetMatcher() {
		matcher.clear();
	}

	public void setLastMatchPrice(String symbol, int price) {
		matcher.setLastMatchPrice(price);
	}

	public String getExpireReport() {
		String result = "";
		for (ExecutionReport rpt : matcher.getExpiredResult()) {
			result += rpt.getOrderID() + ",C;";
		}
		return result.substring(0, result.length() - 1);
	}
}
