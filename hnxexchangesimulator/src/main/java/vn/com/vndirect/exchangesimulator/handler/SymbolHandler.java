package vn.com.vndirect.exchangesimulator.handler;

import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.order.OrderStorage;
import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.datastorage.order.StorageFactory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderFactory;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.processor.CancelOrderProcessor;
import vn.com.vndirect.exchangesimulator.processor.NewOrderProcessor;
import vn.com.vndirect.exchangesimulator.processor.NullProcessor;
import vn.com.vndirect.exchangesimulator.processor.Processor;
import vn.com.vndirect.exchangesimulator.processor.ReplaceOrderProcessor;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class SymbolHandler implements QueueListener {
 
	private static final Logger LOG = Logger.getLogger(SymbolHandler.class);
	
	private ExecutionReportQueue queueOut;
	
	private CancelOrderProcessor cancelOrderProcessor;
	
	private NewOrderProcessor orderProcessor;
	
	private ReplaceOrderProcessor replaceOrderProcessor;
	
	public SymbolHandler(ExecutionReportQueue queueOut, Matcher matcher, String symbol) {
		this.queueOut = queueOut;
		Storage<NewOrderSingle> orderStorage = StorageFactory.getStore(symbol);
		
		this.cancelOrderProcessor = new CancelOrderProcessor(orderStorage, matcher); 
		this.orderProcessor = new NewOrderProcessor(orderStorage, matcher);
		this.replaceOrderProcessor = new ReplaceOrderProcessor(orderStorage, matcher);
	}

	@Override
	public void onEvent(Object order) {
		String type = getType(order);
		
		Processor processor = getProcessor(type);
		
		List<ExecutionReport> reports = processor.process((HnxMessage) order);
		
		updateQueueout(reports);
		
	}

	private void updateQueueout(List<ExecutionReport> reports) {
		for(ExecutionReport report : reports) {
			queueOut.add(report);
		}
	}

	private Processor getProcessor(String type) {
		if (NewOrderSingle.class.getSimpleName().equals(type)) {
			return orderProcessor;
		}
		if (OrderCancelRequest.class.getSimpleName().equals(type)) {
			return cancelOrderProcessor;
		}
		if (OrderReplaceRequest.class.getSimpleName().equals(type)) {
			return replaceOrderProcessor;
		}
		return new NullProcessor();
	}

	private String getType(Object order) {
		return order.getClass().getSimpleName();
	}
	
}
