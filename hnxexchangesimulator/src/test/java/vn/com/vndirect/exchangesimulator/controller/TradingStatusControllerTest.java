package vn.com.vndirect.exchangesimulator.controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.SocketClient;
import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutService;
import vn.com.vndirect.exchangesimulator.datastorage.queue.QueueOutServiceImpl;
import vn.com.vndirect.exchangesimulator.datastorage.queue.TradingSessionStatusRequestQueue;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManager;
import vn.com.vndirect.exchangesimulator.matching.Matcher;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatusRequest;

public class TradingStatusControllerTest {
	
	private QueueOutService queueOutService;
	private TradingSessionStatusRequestQueue tradingQueueIn;
	private TradingSessionStatusController tradingSessionStatusController;
	private TradingSessionStatusManager tradingSessionStatusManager;
	private Matcher matcher = new Matcher() {
		public void beginATC() {};
		public List<ExecutionReport> endATC() {return new ArrayList<>();};
	};
	
	@Before
	public void setUp() {
		tradingSessionStatusManager = new TradingSessionStatusManager() {
			
			@Override
			public void setSession(TradingSessionStatus sessionStatus) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public TradingSessionStatus getPtSession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getPreOpenSession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getOpen2Session() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getOpen1Session() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getIntermissionSession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getEndOfDaySession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getCurrentSession() {
				TradingSessionStatus tradingSessionStatus = new TradingSessionStatus();
				tradingSessionStatus.setTradSesReqID("LIS_BRD_01");
				return tradingSessionStatus;
			}
			
			@Override
			public TradingSessionStatus getCloseSession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TradingSessionStatus getCloseBlSession() {
				// TODO Auto-generated method stub
				return null;
			}
		};
			
		InMemory memory = new InMemory();
		queueOutService = new QueueOutServiceImpl();
		tradingQueueIn = new TradingSessionStatusRequestQueue();
		tradingSessionStatusController = new TradingSessionStatusController(tradingQueueIn, queueOutService, tradingSessionStatusManager, memory, matcher);
		memory.put("last_processed_sequence", "VNDS", 0);
		List<SocketClient> socketClients = new ArrayList<SocketClient>();
		socketClients.add(new SocketClient(new Socket(), "VNDS"));
		memory.put("SocketClientList", "", socketClients);
	}
	
	@Test
	public void testGetSecurityStatusMessage() throws InterruptedException {
		TradingSessionStatusRequest req = new TradingSessionStatusRequest();
		req.setSenderCompID("VNDS");
		tradingQueueIn.add(req);
		tradingSessionStatusController.start();
		Thread.sleep(500);
		TradingSessionStatus msg = (TradingSessionStatus) queueOutService.poll();
		Assert.assertEquals("LIS_BRD_01", msg.getTradSesReqID());
		Assert.assertEquals("VNDS", msg.getTargetCompID());
		Assert.assertNotNull(msg);
		tradingSessionStatusController.stop();
	}
	
}