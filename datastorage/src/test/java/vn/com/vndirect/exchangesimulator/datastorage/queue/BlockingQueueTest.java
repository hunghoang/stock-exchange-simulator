package vn.com.vndirect.exchangesimulator.datastorage.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

public class BlockingQueueTest {
	
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	private Thread t;
	public void init() {
		t = new Thread() {
			@Override
			public void run() {
				Object object;
				try {
					while((object = queue.take()) != null) {
						System.out.println(object);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		t.start();
	}
	
	public void add(String a) {
		queue.add(a);
	}
	
	public void interrupt() {
		t.interrupt();
	}

	@Test
	public void testQueue() {
		new QueueInServiceImpl();
	}
	
	public static void main(String[] args) {
		BlockingQueueTest queue = new BlockingQueueTest();
		queue.init();
		queue.add("a");
		queue.add("b");
		queue.add("c");
		System.out.println("Exit");
		queue.interrupt();
		
	}
}
