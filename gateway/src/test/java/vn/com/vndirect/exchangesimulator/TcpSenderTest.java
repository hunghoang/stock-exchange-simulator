package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.Heartbeat;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.Logon;

public class TcpSenderTest {

	private final List<byte[]> expectedBytes = new ArrayList<byte[]>();
	private Socket socket;
	private InMemory memory;
	private HnxMessage message;
	private QueueOutService queueOutService;
	private TcpSender sender;
	
	@Before
	public void setup() throws IOException {
		memory = new InMemory();
		 final OutputStream outputStream = new OutputStream() {
			
			 @Override
			 public void write(byte[] bytes) throws IOException {
				 expectedBytes.add(bytes);
			 }
			 
			 @Override
			 public void write(int b) throws IOException {
			 }
		};
		
		socket = new Socket() {
			@Override
			public OutputStream getOutputStream() throws IOException {
				return outputStream;
			}
		};
		SocketClient socketClient = new SocketClient(socket,"VNDS");
		memory.put("SocketClient", "VNDS", socketClient);
		expectedBytes.clear();
		message = new Logon();
		
		FixConvertor fixConvertor = new FixConvertor();
		queueOutService = new QueueOutServiceImpl();
		sender = new TcpSender(queueOutService, memory, fixConvertor);
		memory.put("sequence", "VNDS", 0);
		memory.put("last_processed_sequence", "VNDS", 0);
	}

	@Test
	public void sendMessageTestSuccess() throws IOException {
		message.setTargetCompID("VNDS");
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", "VNDS");
		socketClient.setLogon(true);
		sender.send(message);
		Assert.assertEquals(1, expectedBytes.size());
		System.out.println(new String(expectedBytes.get(0)));
		
	}
	
	@Test
	public void sendMessageWhenQueueoutHasData() throws InterruptedException, IOException {
		message.setTargetCompID("VNDS");
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", "VNDS");
		socketClient.setLogon(true);
		sender.start();
		queueOutService.add(message);
		Thread.sleep(1000);
		Assert.assertEquals(1, expectedBytes.size());
	}
	
	@Test
	public void testSendHeartBeat() throws IOException {
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", "VNDS");
		socketClient.setLogon(true);
		Heartbeat hb = new Heartbeat();
		hb.setTargetCompID("VNDS");
		
		memory.put("sequence", "VNDS", 1);
		
		sender.send(hb);
		
		Assert.assertEquals(1, hb.getMsgSeqNum());
		Assert.assertEquals(0, hb.getLastMsgSeqNumProcessed());
	}

	@Test
	public void testSendHeartBeatEvery15Second() throws IOException, InterruptedException {
		List<SocketClient> clients = new ArrayList<SocketClient>();
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", "VNDS");
		socketClient.setLogon(true);
		clients.add(socketClient);
		memory.put("SocketClientList", "", clients);
		sender.setActive(true);
		sender.setHeartBeatDelay(15);
		sender.startSendHeartBeat();
		Thread.sleep(1000);
		Assert.assertTrue(expectedBytes.size() > 0);
		Thread.sleep(1000);
		Assert.assertTrue(expectedBytes.size() > 1);
		Thread.sleep(1000);
		Assert.assertTrue(expectedBytes.size() > 2);
	}
	
}
