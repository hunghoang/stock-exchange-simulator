package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueInServiceImpl;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;

public class TcpConnectionTest {
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testAddSocket() throws IOException {
		String userId = "VNDS";
		String remoteIp = "localhost";
		InMemory memory = new InMemory();
		TcpReceiver tcpReceiver = new TcpReceiver(memory,new QueueInServiceImpl(), new FixConvertor());
		TcpConnection tcpConnection = new TcpConnection(memory, tcpReceiver);
		Socket socket = new Socket();
		tcpConnection.acceptSocket(remoteIp, socket);
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", userId);
		Assert.assertEquals(socket, socketClient.getSocket());
	}
	
	
}
