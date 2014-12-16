package vn.com.vndirect.exchangesimulator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.lib.commonlib.file.FileUtils;

@Component
public class TcpConnection {
	private static final Logger log = Logger.getLogger(TcpConnection.class);

	private final ServerSocket server;

	private volatile boolean isActive;

	private TcpReceiver tcpReceiver;
	private InMemory memory;
	private Properties properties;

	@Autowired
	public TcpConnection(InMemory memory, TcpReceiver tcpReceiver) throws IOException {
		log.info("Init tcp connection");
		isActive = true;
		this.memory = memory;
		this.tcpReceiver = tcpReceiver;
		properties = new Properties();
		properties.load(FileUtils.getInputStream("config/client.properties"));
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
						acceptSocket(remoteIp, socket);
					}
				} catch (IOException e) {
					log.error("Error when waiting connection", e);
				}

			};
		}.start();
	}

	public void acceptSocket(String remoteIp, Socket socket) {
		String userId = (String) properties.get(remoteIp);
		log.info("Accept socket " + userId);
		memory.put("SocketClient", userId, new SocketClient(socket, userId));
		memory.put("sequence", userId, 0);
		memory.put("last_processed_sequence", userId, 0);

	}

}
