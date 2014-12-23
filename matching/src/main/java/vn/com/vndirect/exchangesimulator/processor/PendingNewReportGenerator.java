package vn.com.vndirect.exchangesimulator.processor;

import java.util.Date;

import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

public class PendingNewReportGenerator {
	public static ExecutionReport report(NewOrderSingle message) {
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setTargetCompID(message.getSenderCompID());
		executionReport.setOrderID(message.getOrderId());
		executionReport.setClOrdID(message.getClOrdID());
		executionReport.setPrice(message.getPrice());
		executionReport.setOrderQty(message.getOrderQty());
		executionReport.setSymbol(message.getSymbol());
		executionReport.setAccount(message.getAccount());
		executionReport.setTransactTime(new Date());
		executionReport.setSide(message.getSide());
		executionReport.setOrdStatus(OrdStatus.PENDING_NEW);
		executionReport.setExecType(ExecType.NEW);
		executionReport.setOrdType(message.getOrdType());
		return executionReport;
	}
}
