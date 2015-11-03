package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.fixconvertor.FixConvertor;
import vn.com.vndirect.exchangesimulator.model.Heartbeat;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.Logon;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;
import vn.com.web.commons.exception.SystemException;
import vn.com.web.commons.utility.DateUtils;

@Component
public class TcpSender {

	private static final Logger log = Logger.getLogger(TcpSender.class);
	private InMemory memory;
	private FixConvertor fixConvertor;
	private QueueOutService queueOutService;
	private boolean isActive;
	private int heartbeatDelay = 15000;;

	@Autowired
	public TcpSender(QueueOutService queueOutService, InMemory memory, FixConvertor fixConvertor) {
		this.memory = memory;
		this.fixConvertor = fixConvertor;
		this.queueOutService = queueOutService;
	}

	@PostConstruct
	public void start() {
		isActive = true;
		startThreadSendMessage();
	}

	public void stop() {
		isActive = false;
	}

	private void startThreadSendMessage() {
		new Thread() {
			@Override
			public void run() {
				while (isActive) {
					try {
						HnxMessage message = (HnxMessage) queueOutService.poll();
						if (message == null) {
							Thread.sleep(100);
							continue;
						}
						send(message);
					} catch (InterruptedException | IOException | RuntimeException e) {
						e.printStackTrace();
					} 
				}

			}
		}.start();
	}

	protected void send(HnxMessage message) throws IOException {
		setHeader(message);
		setSequence(message);
		String text = fixConvertor.convertObjectToFix(message);
		String userId = message.getTargetCompID();
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", userId);
		if (socketClient != null) {
			socketClient.send(text);
		} else {
			SocketClient sc = getDefaultSocket();
			if (sc != null) {
				sc.send(text);
			}
		}
	}
	
	private SocketClient getDefaultSocket() {
		Object clients = memory.get("SocketClientList", "");
		if (clients != null) {
			int size = ((ArrayList<SocketClient>) clients).size();
			if (size > 0) {
				return ((ArrayList<SocketClient>) clients).get(size - 1);
			}
		}
		return null;
	}
	
	public void manualSend(String userId, String message) throws IOException {
		SocketClient socketClient = (SocketClient) memory.get("SocketClient", userId);
		socketClient.send(message);
	}

	private void setSequence(HnxMessage message) {
		String userId = message.getTargetCompID();
		Integer seqnum = (Integer) memory.get("sequence", userId);
		Integer lastProccessedSeq = (Integer) memory.get("last_processed_sequence", userId);
		if (isOrderMessage(message)) {
			if (seqnum == null) 
				seqnum = 0;
			memory.put("sequence", userId, ++seqnum);
		}
		message.setMsgSeqNum(seqnum);
		message.setLastMsgSeqNumProcessed(lastProccessedSeq);
	}

	private void setHeader(HnxMessage message) {
		try {
			message.setSendingTime(DateUtils.dateToString(new Date(), "yyyyMMdd-hh:mm:ss"));
			message.setSenderCompID("HNX");
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isOrderMessage(HnxMessage message) {
		if (!(message instanceof Heartbeat || message instanceof Logon)) {
			return true;
		}
		return false;
	}

	public void startSendHeartBeat() {
		new Thread() {
			@Override
			public void run() {
				Heartbeat hb = new Heartbeat();
				while (isActive) {
					try {
						Object clients = memory.get("SocketClientList", "");
						System.out.println(clients);
						if (clients != null) {
							for (SocketClient socketClient : (List<SocketClient>) clients) {
								hb.setTargetCompID(socketClient.getUserId());
								send(hb);
							}
						}
						Thread.sleep(heartbeatDelay);
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}

			}
		}.start();

	}

	protected void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setHeartBeatDelay(int heartbeatDelay) {
		this.heartbeatDelay = heartbeatDelay;

	}

}
