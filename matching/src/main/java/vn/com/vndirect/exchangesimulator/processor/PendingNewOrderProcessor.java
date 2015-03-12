package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
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
		return PendingNewReportGenerator.report(message);
	}
	
	private void storeOrder(final NewOrderSingle newOrderSingle) {
		orderStorage.add(newOrderSingle);
	}
	
}
