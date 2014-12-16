package vn.com.vndirect.exchangesimulator.order.listener;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.generator.OrderQueueOfStockGenerator;
import vn.com.vndirect.exchangesimulator.datastorage.queue.generator.OrderQueueOfStockGeneratorImpl;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

@Ignore
public class MainOrderQueueListenerTest {

	@Before
	public void setup() {
		
	}
	
	
	@Test
	public void testOrderQueueReceiveOrderAndRouteToStockQueue() throws InterruptedException {
		OrderQueue queue = new OrderQueue();
		OrderQueueOfStockGenerator generator = new OrderQueueOfStockGeneratorImpl();
		final List<Object> objectInListener = new ArrayList<Object>();
		
		NewOrderSingle order = new NewOrderSingle();
		String symbol = "VND";
		order.setSymbol(symbol);
		queue.add(order);
		Thread.sleep(1000);
		OrderQueue queueByStock = generator.getQueueByStock(symbol);
		Assert.assertNotNull(queueByStock);
		
		Assert.assertEquals(1, objectInListener.size());
		
		symbol = "SSI";
		order.setSymbol(symbol);
		queue.add(order);
		Thread.sleep(1000);
		OrderQueue queueByStock2 = generator.getQueueByStock(symbol);
		Assert.assertNotNull(queueByStock2);
		
		Assert.assertNotEquals(queueByStock, queueByStock2);
		
		Assert.assertEquals(2, objectInListener.size());
		
	}
}
