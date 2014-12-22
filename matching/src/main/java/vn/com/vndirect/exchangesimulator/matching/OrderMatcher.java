package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class OrderMatcher {
	
	private ReportGenerator reportGenerator;

	public OrderMatcher(ReportGenerator reportGenerator){
		this.reportGenerator = reportGenerator;
	}
	
	public List<ExecutionReport> match(OrderList currList, OrderList nextList){
		List<ExecutionReport> result = new ArrayList<ExecutionReport>();
		for (NewOrderSingle nextOrder : nextList.items()) {
			List<ExecutionReport> rps = match(nextOrder, currList);
			currList.clearEmptyOrder();
			result.addAll(rps);
			if(nextOrder.getOrderQty() > 0){
				break;
			}
		}
		nextList.clearEmptyOrder();
		return result;
	}
	
	public List<ExecutionReport> match(NewOrderSingle order, OrderList list) {
		List<ExecutionReport> result = new ArrayList<ExecutionReport>();
		for (NewOrderSingle pendingOrder : list.items()) {
			List<ExecutionReport> rps = match(pendingOrder, order);
			result.addAll(rps);
			if (order.getOrderQty() == 0) {
				break;
			}
		}
		return result;
	}

	private void updateMatchedOrder(NewOrderSingle currentOrder, NewOrderSingle newOrder) {
		int currQuantity = currentOrder.getOrderQty();
		int newQuantity = newOrder.getOrderQty();
		int minQty = Math.min(currQuantity, newQuantity);

		currentOrder.setOrderQty(currQuantity - minQty);
		newOrder.setOrderQty(newQuantity - minQty);
	}

	private List<ExecutionReport> match(NewOrderSingle currOrder, NewOrderSingle nextOrder) {
		List<ExecutionReport> result = reportGenerator.createReport(currOrder, nextOrder);
		updateMatchedOrder(currOrder, nextOrder);
		return result;
	}

}