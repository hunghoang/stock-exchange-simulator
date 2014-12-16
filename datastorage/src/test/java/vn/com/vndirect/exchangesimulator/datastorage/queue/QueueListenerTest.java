package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QueueListenerTest {

	@Test
	public void testListener() throws InterruptedException {
		final List<Object> objects = new ArrayList<Object>();
		QueueInServiceImpl service = new QueueInServiceImpl();
		service.setOthersQueue(new OthersQueue());
		service.addListener(new QueueListener() {
			public void onEvent(Object source) {
				objects.add(source);
			}
		});
		
		service.addListener(new QueueListener() {
			public void onEvent(Object source) {
				objects.add(source);
			}
		});
		service.add(1);
		service.add(2);
		Thread.sleep(100);
		Assert.assertEquals(4, objects.size());
		Assert.assertTrue(objects.contains(1));
		Assert.assertTrue(objects.contains(2));
	}
}
