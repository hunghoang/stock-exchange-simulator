package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.builder.FixMessageBuilder;
import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueInService;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;
import vn.com.vndirect.exchangesimulator.model.Logon;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.OrderReplaceRequest;
import vn.com.vndirect.exchangesimulator.model.SecurityStatusRequest;
import vn.com.vndirect.exchangesimulator.model.TestRequest;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;

@Component
public class TcpReceiver {

	private static final Logger log = Logger.getLogger(TcpReceiver.class);
	
	private List<Socket> sockets = new ArrayList<Socket>();

	private InMemory memory;
	
	private boolean isActive;
	
	private QueueInService queueInService;
	private FixConvertor fixConvertor;
	@Autowired
	public TcpReceiver(InMemory memory, QueueInService queueInService, FixConvertor fixConvertor) {
		this.memory = memory;
		this.queueInService = queueInService;
		this.fixConvertor = fixConvertor;
	}
	
	@PostConstruct
	public void start() {
		isActive = true;
	}
	
	public void stop() {
		isActive = false;
	}

	public void addSocket(Socket socket) throws IOException {
		if (isActive) {
			startSocket(socket);
		}
		sockets.add(socket);
	}

	private void startSocket(Socket socket) throws IOException {
		final InputStream inputStream = socket.getInputStream();		
		Thread receiveThread = new Thread() {			
			@Override
			public void run() {
				FixMessageBuilder builder = new FixMessageBuilder();
				int bytesRead;
				byte[] buffer = new byte[1024];
				try {
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						byte[] receivedBytes = new byte[bytesRead]; 
						System.arraycopy(buffer, 0, receivedBytes, 0, bytesRead);
						List<String> messages = builder.receive(receivedBytes);
						pushToQueue(messages);
						buffer = new byte[1024];
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			

		};
		receiveThread.start();
	}
	
	protected void pushToQueue(List<String> messages) {
		for (String message : messages) {
			Object object = convertFixToObject(message);
			log.info("EX <--: " + message);
			queueInService.add(object);
		}
	}

	private Object convertFixToObject(String message) {
		if (isLogonMessage(message)) {
			return fixConvertor.convertFixToObject(message, Logon.class);
		}
		
		if (isSecurityStatusRequestMessage(message)) {
			return fixConvertor.convertFixToObject(message, SecurityStatusRequest.class);
		}
		
		if (isTradingSessionStatusRequestMessage(message)) {
			return fixConvertor.convertFixToObject(message, TradingSessionStatusRequest.class);
		}

		if (isNewOrderSingle(message)) {
			return fixConvertor.convertFixToObject(message, NewOrderSingle.class);
		}

		if (isOrderReplaceRequest(message)) {
			return fixConvertor.convertFixToObject(message,OrderReplaceRequest.class);
		}

		if (isOrderCancelRequest(message)) {
			return fixConvertor.convertFixToObject(message,OrderCancelRequest.class);
		}
		
		if (isTestRequestMessage(message)) {
			return fixConvertor.convertFixToObject(message,TestRequest.class);
		}

		
		return message;
	}

	private boolean isTestRequestMessage(String message) {
		return message.indexOf("35=1") > 0;
	}

	private boolean isOrderCancelRequest(String message) {
		return message.indexOf("35=F") > 0;
	}

	private boolean isOrderReplaceRequest(String message) {
		return message.indexOf("35=G") > 0;
	}

	private boolean isNewOrderSingle(String message) {
		return message.indexOf("35=D") > 0;
	}

	private boolean isTradingSessionStatusRequestMessage(String message) {
		return message.indexOf("35=g") > 0;
	}

	private boolean isSecurityStatusRequestMessage(String message) {
		return message.indexOf("35=e") > 0;
	}

	private boolean isLogonMessage(String message) {
		return message.indexOf("35=A") > 0;
	}

	public List<Socket> getSocketList() {
		return sockets;
	}

}
