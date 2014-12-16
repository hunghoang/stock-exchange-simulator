package vn.com.vndirect.exchangesimulator.router;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.queue.ExecutionReportQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;
import vn.com.vndirect.exchangesimulator.datastorage.queue.generator.OrderQueueOfStockGenerator;
import vn.com.vndirect.exchangesimulator.handler.SymbolHandler;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;

@Component
public class SymbolRouter implements QueueListener {
	
	private static final Logger LOGGER = Logger.getLogger(SymbolRouter.class);
	
	private OrderQueue queueIn;
	
	private OrderQueueOfStockGenerator queueGenerator;
	
	private ExecutionReportQueue queueOut;
	
	private Matcher matcher;
	
	@Autowired
	public SymbolRouter(OrderQueue queueIn, OrderQueueOfStockGenerator queueGenerator, ExecutionReportQueue queueOut, Matcher matcher) {
		this.queueIn = queueIn;
		this.queueGenerator = queueGenerator;
		this.queueOut = queueOut;
		this.matcher = matcher;
		this.queueIn.addListener(this);
	}

	@Override
	public void onEvent(Object order) {
		String symbol = getSymbolOfOrder(order);
		addOrderToQueueOfStock(symbol, order);
	}
	
	private String getSymbolOfOrder(Object order) {
		String symbol = null;
		if (NewOrderSingle.class.isInstance(order)) {
			symbol = ((NewOrderSingle) order).getSymbol();
		} else if (OrderCancelRequest.class.isInstance(order)) {
			symbol = ((OrderCancelRequest) order).getSymbol();
		} else if (OrderReplaceRequest.class.isInstance(order)) {
			symbol = ((OrderReplaceRequest) order).getSymbol();
		}
		return symbol;
	}

	private void addOrderToQueueOfStock(String symbol, Object order) {
		if (StringUtils.isNotBlank(symbol)) {
			OrderQueue queue = getQueue(symbol);
			routeToQueue(order, queue);
		}
	}

	private void routeToQueue(Object order, OrderQueue queue) {
		queue.add(order);
	}

	private OrderQueue getQueue(String symbol) {
		OrderQueue queue = queueGenerator.getQueueByStock(symbol);
		if (queue == null) {
			queue = queueGenerator.createQueueOfStockWithListener(symbol, new SymbolHandler(queueOut, matcher, symbol));
		}
		return queue;
	}
}
