package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class NewOrderProcessor implements Processor {

	private Storage<NewOrderSingle> orderStorage;
	
	private Matcher matcher;
	
	public NewOrderProcessor(Storage<NewOrderSingle> orderStorage, Matcher matcher) {
		this.orderStorage = orderStorage;
		this.matcher = matcher; 
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		List<ExecutionReport> executionReports = new ArrayList<ExecutionReport>();
		NewOrderSingle newOrderSingle = (NewOrderSingle) message; 
		updateOrgiQty(newOrderSingle);
		storeOrder(newOrderSingle);
		
		ExecutionReport executionReport = buildConfirmOrder(newOrderSingle);
		executionReports.add(executionReport);
		
		List<ExecutionReport> matchedExecutionReports = matcher.push(newOrderSingle);
		cleanFilledOrder(matchedExecutionReports);
		executionReports.addAll(matchedExecutionReports);
		
		return executionReports;
	}

	private void updateOrgiQty(NewOrderSingle newOrderSingle) {
		newOrderSingle.setOrgiQty(newOrderSingle.getOrderQty());
	}

	protected ExecutionReport buildConfirmOrder(NewOrderSingle message) {
		ExecutionReport executionReport = new ExecutionReport();
		String orderId = message.getOrderId();
		message.setOrderId(orderId);
		
		executionReport.setTargetCompID(message.getSenderCompID());
		executionReport.setOrderID(orderId);
		executionReport.setClOrdID(message.getClOrdID());
		executionReport.setPrice(message.getPrice());
		executionReport.setOrderQty(message.getOrderQty());
		executionReport.setSymbol(message.getSymbol());
		executionReport.setAccount(message.getAccount());
		executionReport.setTransactTime(new Date());
		executionReport.setSide(message.getSide());
		executionReport.setOrdStatus('A');
		executionReport.setExecType('0');
		executionReport.setOrdType(message.getOrdType());
		return executionReport;
	}
	
	private void storeOrder(final NewOrderSingle newOrderSingle) {
		orderStorage.add(newOrderSingle);
	}
	
	private void cleanFilledOrder(List<ExecutionReport> executionReports) {
		for (ExecutionReport executionReport : executionReports) {
			String sellOrderId = executionReport.getOrigClOrdID();
			String buyOrderId = executionReport.getSecondaryClOrdID();
			NewOrderSingle sellOrder = orderStorage.get(sellOrderId);
			NewOrderSingle buyOrder = orderStorage.get(buyOrderId);
			
			if (isOrderFilled(sellOrder)) {
				orderStorage.remove(sellOrder.getOrderId());
			}
			if (isOrderFilled(buyOrder)) {
				orderStorage.remove(buyOrder.getOrderId());
			}
		}
		
	}

	private boolean isOrderFilled(NewOrderSingle order) {
		return order != null && order.getOrderQty() == 0;
	}

}
