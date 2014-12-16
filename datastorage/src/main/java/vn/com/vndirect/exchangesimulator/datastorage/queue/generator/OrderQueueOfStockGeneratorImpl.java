package vn.com.vndirect.exchangesimulator.datastorage.queue.generator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;

@Component
public class OrderQueueOfStockGeneratorImpl implements OrderQueueOfStockGenerator {

	private Map<String, OrderQueue> queues = new HashMap<String, OrderQueue>();

	public OrderQueue getQueueByStock(String stock) {
		return queues.get(stock);
	}

	public OrderQueue createQueueOfStockWithListener(String stock, QueueListener queueListener) {
		OrderQueue queue = queues.get(stock);
		if (queue == null) {
			queue = new OrderQueue();
			queue.addListener(queueListener);
			queues.put(stock, queue);
		}
		return queue;
	}

}
