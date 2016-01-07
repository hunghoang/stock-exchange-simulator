package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TcpConnection {
	private static final Logger log = Logger.getLogger(TcpConnection.class);

	private final ServerSocket server;

	private volatile boolean isActive;

	private TcpReceiver tcpReceiver;

	@Autowired
	public TcpConnection(TcpReceiver tcpReceiver) throws IOException {
		isActive = true;
		this.tcpReceiver = tcpReceiver;
		server = new ServerSocket(6666);
	}

	@PostConstruct
	public void start() throws IOException {
		log.info("Server waiting connection");
		new Thread() {
			public void run() {
				try {
					while (isActive) {
						Socket socket = server.accept();
						String remoteIp = socket.getInetAddress().getHostAddress();
						log.info("Accepting connection from: " + remoteIp);	
						tcpReceiver.addSocket(socket);
					}
				} catch (IOException e) {
					log.error("Error when waiting connection", e);
				}
			};
		}.start();
	}

	

}
