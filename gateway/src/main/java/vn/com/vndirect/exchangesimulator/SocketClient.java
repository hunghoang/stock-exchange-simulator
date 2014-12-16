package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

public class SocketClient {
	private static final Logger log = Logger.getLogger(SocketClient.class);
	private Socket socket;
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private boolean isLogon;
	
	public void setLogon(boolean isLogon) {
		this.isLogon = isLogon;
	}

	public SocketClient(Socket socket, String userId) {
		this.userId = userId;
		this.socket = socket;
	}

	public boolean isLogon() {
		return isLogon;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void send(String text) throws IOException {
		if (isLogon) {
			log.info("EX -->: " + text);
			socket.getOutputStream().write(text.getBytes());
			socket.getOutputStream().flush();
		}
	}


}
