package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.TradSesStatus;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

public class TradingSessionStatusManagerTest {
	
	private ObjectConvertor convertor;
	private TradingSessionStatusManagerImpl tradingSessionStatusManager;
	private TradingSessionStatus tradingSessionStatus;
	
	@Before
	public void setUp() {
		convertor = new ObjectConvertor();
		tradingSessionStatusManager = new TradingSessionStatusManagerImpl(convertor);
	}
	
	@Test
	public void shouldMakePreOpenSessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getPreOpenSession();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 90);
		Assert.assertEquals(TradSesStatus.PREOPEN, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakeOpen1SessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getOpen1Session();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertTrue("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID()));
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 1);
		Assert.assertEquals(TradSesStatus.LO, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakeIntermissionSessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getIntermissionSession();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertTrue("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID()));
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 2);
		Assert.assertEquals(TradSesStatus.INTERMISSION, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakeOpen2SessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getOpen2Session();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertTrue("LIS_CON_NML".equals(tradingSessionStatus.getTradingSessionID()));
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 1);
		Assert.assertEquals(TradSesStatus.LO, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakeCloseSessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getCloseSession();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertTrue("LIS_AUC_C_NML".equals(tradingSessionStatus.getTradingSessionID()));
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 1);
		Assert.assertEquals(TradSesStatus.ATC1, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakeCloseBlSessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getCloseBlSession();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertTrue("LIS_AUC_C_NML_LOC".equals(tradingSessionStatus.getTradingSessionID()));
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 1);
		Assert.assertEquals(TradSesStatus.ATC2, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakePtSessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getPtSession();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertTrue("LIS_PTH_P_NML".equals(tradingSessionStatus.getTradingSessionID()));
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 1);
		Assert.assertEquals(TradSesStatus.PTCLOSE, tradingSessionStatus.getTradingSessionCode());
	}
	
	@Test
	public void shouldMakeEndOfDaySessionStatus() {
		tradingSessionStatus = tradingSessionStatusManager.getEndOfDaySession();
		Assert.assertNotNull(tradingSessionStatus);
		Assert.assertEquals(tradingSessionStatus.getTradSesStatus(), 97);
		Assert.assertEquals(TradSesStatus.ENDOFDAY, tradingSessionStatus.getTradingSessionCode());
	}
}
