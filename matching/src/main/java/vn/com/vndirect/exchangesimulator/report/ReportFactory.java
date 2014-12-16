package vn.com.vndirect.exchangesimulator.report;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ReportFactory {
	
	public static ExecutionReport genMatchedReport(NewOrderSingle sampleOrder, String orderID, double price, int quantity) {
		ExecutionReport report = new ExecutionReport();
		report.setClOrdID(sampleOrder.getClOrdID());
		report.setOrigClOrdID(sampleOrder.getOrderId());
		report.setSymbol(sampleOrder.getSymbol());
		report.setLastPx(price);
		report.setOrderID(orderID);
		report.setLastQty(quantity);
		report.setSenderCompID("HNX");
		report.setOrdStatus('2');
		report.setExecType('3');
		return report;
	}
}