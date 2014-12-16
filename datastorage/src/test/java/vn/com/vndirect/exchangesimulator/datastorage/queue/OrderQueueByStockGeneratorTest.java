package vn.com.vndirect.exchangesimulator.datastorage.queue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.queue.generator.OrderQueueOfStockGenerator;
import vn.com.vndirect.exchangesimulator.datastorage.queue.generator.OrderQueueOfStockGeneratorImpl;

public class OrderQueueByStockGeneratorTest {
	private OrderQueueOfStockGenerator stockQueueGenerator;
	
	@Before
	public void setUp() {
		stockQueueGenerator = new OrderQueueOfStockGeneratorImpl();
	}

	@Test
	public void testStockQueueGeneratorReturnNullWhenQueueNotCreated() {
		AbstractQueueService queueService = stockQueueGenerator.getQueueByStock("VND");
		Assert.assertEquals(null, queueService);
	}
	
	@Test
	public void testStockQueueGeneratorCreateManyQueue() {
		String queueName = "VND";
		AbstractQueueService queueService = createQueue("VND");
		for(int i = 0; i < 1000; i++) {
			queueName = "VND" + i;
			AbstractQueueService queueService1 = createQueue(queueName);
			AbstractQueueService queueService2 = stockQueueGenerator.getQueueByStock(queueName);
			Assert.assertNotEquals(queueService, queueService1);
			Assert.assertEquals(queueService1, queueService2);
		}
	}
	
	@Test
	public void testCreanteStockQueueAndAddListner() {
		AbstractQueueService queueService = createQueue("VND");
		AbstractQueueService queueService1 = stockQueueGenerator.getQueueByStock("VND");
		Assert.assertEquals(queueService, queueService1);
	}
	
	private AbstractQueueService createQueue(String name) {
		return stockQueueGenerator.createQueueOfStockWithListener(name, new QueueListener() {
			@Override
			public void onEvent(Object source) {
			}
		});
	}
}
