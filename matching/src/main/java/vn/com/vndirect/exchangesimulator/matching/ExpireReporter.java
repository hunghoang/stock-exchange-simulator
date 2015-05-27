package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

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
		report.setOrdStatus(OrdStatus.REJECT);
		report.setExecType(ExecType.REJECT);
		report.setUnderlyingLastQty(order.getOrderQty());
		report.setOrdRejReason("4");
		report.setRefSeqNum(order.getMsgSeqNum());
		report.setOrdType(order.getOrdType());
		report.setClOrdID(order.getClOrdID());
		report.setTransactTime(new Date());
		report.setSide(order.getSide());
		report.setOrderID(order.getOrderId());
		return report;
	}

}
