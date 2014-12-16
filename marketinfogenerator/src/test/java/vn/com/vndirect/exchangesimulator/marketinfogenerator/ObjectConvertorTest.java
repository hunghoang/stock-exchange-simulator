package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.Heartbeat;
import vn.com.vndirect.exchangesimulator.model.Logon;
import vn.com.vndirect.exchangesimulator.model.ResendRequest;
import vn.com.vndirect.exchangesimulator.model.SequenceReset;
import vn.com.vndirect.exchangesimulator.model.TestRequest;
import vn.com.vndirect.exchangesimulator.model.TradingSessionStatus;

public class ObjectConvertorTest {
	
	private ObjectConvertor objectConvertor;
	
	@Before
	public void setUp() {
		objectConvertor = new ObjectConvertor();
	}
	
	@Test
	public void convertLogonMessageTest() throws Exception {
		String sourceFile = "logon.csv";
		List<Logon> listMesg = objectConvertor.convertFromCSVFile(sourceFile, Logon.class);
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
		Logon firstMesg = (Logon) listMesg.get(0);
		Assert.assertEquals(firstMesg.getMsgType(), "A");
	}
	
	@Test
	public void convertResendRequestMessageTest() throws Exception {
		String sourceFile = "resendrequest.csv";
		List<ResendRequest> listMesg = objectConvertor.convertFromCSVFile(sourceFile, ResendRequest.class);
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
		ResendRequest firstMesg = (ResendRequest) listMesg.get(0);
		Assert.assertEquals(firstMesg.getMsgType(), "2");
	}
	
	@Test
	public void convertHeartbeatMessageTest() throws Exception {
		String sourceFile = "heartbeat.csv";
		List<Heartbeat> listMesg = objectConvertor.convertFromCSVFile(sourceFile, Heartbeat.class);
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
		Heartbeat firstMesg = (Heartbeat) listMesg.get(0);
		Assert.assertEquals(firstMesg.getMsgType(), "0");
	}
	
	@Test
	public void convertSequenceResetMessageTest() throws Exception {
		String sourceFile = "sequencereset.csv";
		List<SequenceReset> listMesg = objectConvertor.convertFromCSVFile(sourceFile, SequenceReset.class);
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
		SequenceReset firstMesg = (SequenceReset) listMesg.get(0);
		Assert.assertEquals(firstMesg.getMsgType(), "4");
	}
	
	@Test
	public void convertTestRequestMessageTest() throws Exception {
		String sourceFile = "testrequest.csv";
		List<TestRequest> listMesg = objectConvertor.convertFromCSVFile(sourceFile, TestRequest.class);
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
		TestRequest firstMesg = (TestRequest) listMesg.get(0);
		Assert.assertEquals(firstMesg.getMsgType(), "1");
	}
	
	@Test
	public void convertTestTradingSessionStatusMessageTest() throws Exception {
		String sourceFile = "tradingsessionstatus.csv";
		List<TradingSessionStatus> listMesg = objectConvertor.convertFromCSVFile(sourceFile, TradingSessionStatus.class);
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
		TradingSessionStatus firstMesg = (TradingSessionStatus) listMesg.get(0);
		Assert.assertEquals(firstMesg.getMsgType(), "h");
	}
}
