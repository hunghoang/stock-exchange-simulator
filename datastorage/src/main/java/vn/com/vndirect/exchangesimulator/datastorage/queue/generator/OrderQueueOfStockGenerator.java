package vn.com.vndirect.exchangesimulator.datastorage.queue.generator;

import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;

public interface OrderQueueOfStockGenerator {

	OrderQueue getQueueByStock(String stock);

	OrderQueue createQueueOfStockWithListener(String stock, QueueListener queueListener);

}
