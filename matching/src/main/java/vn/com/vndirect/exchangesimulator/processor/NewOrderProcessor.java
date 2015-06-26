package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorageService;
import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class NewOrderProcessor implements Processor {

	private Storage<NewOrderSingle> orderStorage;
	
	private Matcher matcher;
	
	private static final Logger log = Logger.getLogger(NewOrderProcessor.class);
	
	public NewOrderProcessor(Storage<NewOrderSingle> orderStorage, Matcher matcher) {
		this.orderStorage = orderStorage;
		this.matcher = matcher; 
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		List<ExecutionReport> executionReports = new ArrayList<ExecutionReport>();
		NewOrderSingle newOrderSingle = (NewOrderSingle) message; 
		storeOrder(newOrderSingle);
		log.info("List Order size of " + newOrderSingle.getSymbol() + " " + orderStorage.size());
		
		List<ExecutionReport> matchedExecutionReports = matcher.push(newOrderSingle);
		cleanFilledOrder(matchedExecutionReports);
		executionReports.addAll(matchedExecutionReports);
		log.info("List Order size of " + newOrderSingle.getSymbol() + " " + orderStorage.size());
		log.info("All order: " + new OrderStorageService().getAllOrder().size());
		return executionReports;
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
			
			removeOrderIfFilled(sellOrder);
			removeOrderIfFilled(buyOrder);
		}
		
	}

	private void removeOrderIfFilled(NewOrderSingle order) {
		if (isOrderFilled(order)) {
			orderStorage.remove(order.getOrderId());
		}
	}

	private boolean isOrderFilled(NewOrderSingle order) {
		return order != null && order.getOrderQty() == 0;
	}

}
