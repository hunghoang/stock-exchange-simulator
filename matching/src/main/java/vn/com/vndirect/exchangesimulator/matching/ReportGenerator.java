package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

abstract class ReportGenerator {
	abstract List<ExecutionReport> createReport(NewOrderSingle currOrder, NewOrderSingle nextOrder);
	
	protected ExecutionReport genReport(NewOrderSingle order) {
		ExecutionReport rp = new ExecutionReport();
		rp.setTargetCompID(order.getSenderCompID());
		String orderID = order.getOrderId() + (System.currentTimeMillis() % 1000);
		rp.setOrderID(orderID);
		rp.setExecID(orderID);
		rp.setExecType('3');
		rp.setSymbol(order.getSymbol());
		rp.setOrderQty(order.getOrderQty());
		rp.setSide(order.getSide());
		rp.setAccount(order.getAccount());
		rp.setPrice(order.getPrice());
		return rp;
	}
	
	protected List<ExecutionReport> genList(ExecutionReport... rps) {
		List<ExecutionReport> list = new ArrayList<>();
		for (ExecutionReport t : rps) {
			list.add(t);
		}
		return list;
	}
	
	
}
