package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class PendingNewOrderProcessor implements Processor {

	private Storage<NewOrderSingle> orderStorage;
	
	public PendingNewOrderProcessor(Storage<NewOrderSingle> orderStorage) {
		this.orderStorage = orderStorage;
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		List<ExecutionReport> executionReports = new ArrayList<ExecutionReport>();
		NewOrderSingle newOrderSingle = (NewOrderSingle) message; 
		storeOrder(newOrderSingle);
		
		ExecutionReport executionReport = buildConfirmOrder(newOrderSingle);
		executionReports.add(executionReport);
		
		return executionReports;
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
	
}
