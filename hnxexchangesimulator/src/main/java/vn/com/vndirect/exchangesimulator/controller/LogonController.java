package vn.com.vndirect.exchangesimulator.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.SocketClient;
import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.LogonQueue;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.model.Logon;

@Component
public class LogonController extends GatewayMessageController<Logon, Logon> {
	
	private static final Logger log = Logger.getLogger(LogonController.class);
	
	private InMemory memory;

	@Autowired
	public LogonController(LogonQueue queueIn, QueueOutService<Logon> queueOutService, InMemory inMemory) {
		super(queueIn, queueOutService);
		this.memory = inMemory;
	}

	@Override
	protected Logon process(Logon logon) {
		Logon acceptLogon = generateAcceptLogon(logon);
		addSocketClient(acceptLogon.getTargetCompID());
		return acceptLogon;
	}

	protected Logon generateAcceptLogon(Logon logon) {
		String senderId = logon.getSenderCompID();
		Logon acceptLogon = new Logon();
		acceptLogon.setTargetCompID(senderId);
		acceptLogon.setText("Accept logon");
		return acceptLogon;
	}

	private void addSocketClient(String user) {
		SocketClient client = (SocketClient) memory.get("SocketClient", user);
		if (client != null) {
			log.info("Accept logon for user: " + user);
			Object clients = memory.get("SocketClientList", "");
			if (clients == null) {
				clients = new ArrayList<SocketClient>();
				memory.put("SocketClientList", "", clients);
			}
			((List<SocketClient>) clients).add(client);
			client.setLogon(true);
		} else {
			log.error("User not found: " + user);
		}
	}
}

