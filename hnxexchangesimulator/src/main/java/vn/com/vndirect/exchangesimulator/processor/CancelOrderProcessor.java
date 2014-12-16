package vn.com.vndirect.exchangesimulator.processor;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;

public class CancelOrderProcessor implements Processor {
	private final static Logger LOG = Logger.getLogger(CancelOrderProcessor.class);
	private Storage<NewOrderSingle> storage;
	private Matcher matcher;
	public CancelOrderProcessor(Storage<NewOrderSingle> orderStorage, Matcher matcher) {
		this.storage = orderStorage;
		this.matcher = matcher;
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		OrderCancelRequest request = (OrderCancelRequest)message;
		ExecutionReport report = cancelOrder(request);
		removeOrder(request);
		return Collections.singletonList(report);
	}
	
	private void removeOrder(OrderCancelRequest request) {
		storage.remove(request.getOrigClOrdID());
	}

	private ExecutionReport cancelOrder(OrderCancelRequest request) {
		String originClOrdId = request.getOrigClOrdID();
		NewOrderSingle newOrderSingle = storage.get(originClOrdId);
		if (newOrderSingle == null) {
			LOG.error("Root order does not exist for request: " + request);
			return new ExecutionReport();
		}
		
		ExecutionReport executionReport = buildCanceledExecutionReport(request, newOrderSingle);
		matcher.cancelOrder(newOrderSingle);
		return executionReport;
	}

	private ExecutionReport buildCanceledExecutionReport(OrderCancelRequest request, NewOrderSingle newOrderSingle) {
		ExecutionReport executionReport = new ExecutionReport();
		executionReport.setTargetCompID(newOrderSingle.getSenderCompID());
		executionReport.setExecType('4');
		executionReport.setOrdStatus('3');
		executionReport.setOrderID('f'+ newOrderSingle.getOrderId());
		executionReport.setLeavesQty(newOrderSingle.getOrderQty());
		executionReport.setClOrdID(request.getClOrdID());
		executionReport.setOrigClOrdID(request.getOrigClOrdID());
		executionReport.setSymbol(newOrderSingle.getSymbol());
		executionReport.setSide(newOrderSingle.getSide());
		executionReport.setOrdType(newOrderSingle.getOrdType());
		executionReport.setAccount(newOrderSingle.getAccount());
		return executionReport;
	}
	
	

}
