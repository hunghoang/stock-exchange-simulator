package vn.com.vndirect.exchangesimulator.marketinfogenerator;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.datastorage.memory.InMemory;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

public class SecurityStatusManagerTest {
	private ObjectConvertor convertor;
	private SecurityStatusManager securityStatusManager;
	private List<SecurityStatus> listMesg;
	
	@Before
	public void setUp() throws IOException {
		convertor = new ObjectConvertor();
		securityStatusManager = new SecurityStatusManagerImpl(convertor);
		securityStatusManager.setInmemory(new InMemory());
	}
	
	@Test
	public void shouldMakePreOpenTradingSessionStatus() {
		listMesg = securityStatusManager.getSecurityStatus();
		Assert.assertNotNull(listMesg);
		Assert.assertTrue(listMesg.size() > 0);
	}
	
	@Test
	public void shouldGetTradingSessionStatusFromFile() throws ParseException {
		listMesg = securityStatusManager.getSecurityStatus();
		SecurityStatus securityStatus = listMesg.get(0);
		securityStatus.setLastMsgSeqNumProcessed(10);
		Assert.assertEquals(10, securityStatus.getLastMsgSeqNumProcessed());
		Assert.assertEquals(17, securityStatus.getSecurityTradingStatus());
	}
}
