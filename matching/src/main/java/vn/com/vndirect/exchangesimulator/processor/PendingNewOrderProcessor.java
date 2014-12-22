package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

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
	
	private void storeOrder(final NewOrderSingle newOrderSingle) {
		orderStorage.add(newOrderSingle);
	}
	
}
