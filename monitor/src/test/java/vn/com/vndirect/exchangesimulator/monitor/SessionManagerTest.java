package vn.com.vndirect.exchangesimulator.monitor;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.marketinfogenerator.ObjectConvertor;
import vn.com.vndirect.exchangesimulator.marketinfogenerator.TradingSessionStatusManagerImpl;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

public class SessionManagerTest {
	private SessionManagerService sessionManager;
	private TradingSessionStatus tradingSessionStatus;
	
	@Before
	public void setUp() {
		sessionManager = new SessionManagerService(new TradingSessionStatusManagerImpl(new ObjectConvertor()));
	}
	
	@Test
	public void getPreOpenSessionTest() {
		tradingSessionStatus = sessionManager.getSession("preopen");
		Assert.assertTrue("PREOPEN".equals(tradingSessionStatus.getTradingSessionCode().toString()));
	}
}
