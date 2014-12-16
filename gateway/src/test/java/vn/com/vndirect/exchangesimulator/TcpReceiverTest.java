package vn.com.vndirect.exchangesimulator;

import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.LogonQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.OrderQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueInServiceImpl;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueListener;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;


public class TcpReceiverTest {

	private InMemory memory;
	private TcpReceiver tcpReceiver;
	private QueueInServiceImpl queueInService;
	
	private List<Object> messages;
	@Before
	public void setUp() throws Exception {
		memory = new InMemory();
		messages = new ArrayList<Object>();
		queueInService = new QueueInServiceImpl();
		queueInService.setOrderQueue(new OrderQueue());
		queueInService.setLogonQueue(new LogonQueue());
		queueInService.addListener(new QueueListener() {
			
			@Override
			public void onEvent(Object message) {
				messages.add(message);
			}
		});
		FixConvertor convertor = new FixConvertor();
		tcpReceiver = new TcpReceiver(memory,queueInService, convertor);
	}
	
	
	@Test
	public void testAddSocket() throws IOException {
		Socket socket = new Socket();
		tcpReceiver.addSocket(socket);
		assertTrue("Socket is not in list socket of TCP receiver", tcpReceiver.getSocketList().contains(socket));
	}
	
	@Test
	public void testPushQueueInWhenReceiveData() throws InterruptedException {
		List<String> messages = new ArrayList<>();
		String message = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138";
		messages.add(message);
		tcpReceiver.pushToQueue(messages);
		Thread.sleep(1000);
		Assert.assertEquals(1, messages.size());
	}
	
	
}
