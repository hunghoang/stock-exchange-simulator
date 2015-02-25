package vn.com.vndirect.exchangesimulator.handler;

import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.datastorage.order.StorageFactory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.processor.CancelOrderProcessor;
import vn.com.vndirect.exchangesimulator.processor.NewOrderProcessor;
import vn.com.vndirect.exchangesimulator.processor.NullProcessor;
import vn.com.vndirect.exchangesimulator.processor.PendingNewOrderProcessor;
import vn.com.vndirect.exchangesimulator.processor.Processor;
import vn.com.vndirect.exchangesimulator.processor.ReplaceOrderProcessor;

public class SymbolHandler implements QueueListener {
 
	private static final Logger LOGGER = Logger.getLogger(SymbolHandler.class);
	
	private ExecutionReportQueue queueOut;
	
	private CancelOrderProcessor cancelOrderProcessor;
	
	private NewOrderProcessor orderProcessor;
	
	private ReplaceOrderProcessor replaceOrderProcessor;
	
	private PendingNewOrderProcessor pendingNewOrderProcessor;
	
	private final Processor nullProcessor = new NullProcessor();
	
	public SymbolHandler(ExecutionReportQueue queueOut, Matcher matcher, String symbol) {
		this.queueOut = queueOut;
		Storage<NewOrderSingle> orderStorage = StorageFactory.getStore(symbol);
		
		this.cancelOrderProcessor = new CancelOrderProcessor(orderStorage, matcher); 
		this.orderProcessor = new NewOrderProcessor(orderStorage, matcher);
		this.replaceOrderProcessor = new ReplaceOrderProcessor(orderStorage, matcher);
		this.pendingNewOrderProcessor = new PendingNewOrderProcessor(orderStorage);
	}

	@Override
	public void onEvent(Object order) {
		String type = getType(order);
		
		preProcessor(order, type);
		
		matchingProcessor(order, type);
	}

	private void preProcessor(Object order, String type) {
		LOGGER.info("pre process order: " + order + " with type: " + type);
		Processor preProcessor = getPreProcessor(type);
		List<ExecutionReport> reports = preProcessor.process((HnxMessage) order);
		updateQueueout(reports);
	}
	
	private void matchingProcessor(Object order, String type) {
		Processor processor = getProcessor(type);
		List<ExecutionReport> reports = processor.process((HnxMessage) order);
		updateQueueout(reports);
	}

	private Processor getPreProcessor(String type) {
		if (NewOrderSingle.class.getSimpleName().equals(type)) {
			return pendingNewOrderProcessor;
		}
		return nullProcessor;
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
		return nullProcessor;
	}

	private String getType(Object order) {
		return order.getClass().getSimpleName();
	}
	
}
