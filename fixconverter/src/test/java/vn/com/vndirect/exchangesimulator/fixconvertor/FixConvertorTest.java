package vn.com.vndirect.exchangesimulator.fixconvertor;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.SecurityStatus;

public class FixConvertorTest {
	
	private FixConvertor fixConvertor;
	
	@Before
	public void setUp() throws IOException{
		fixConvertor = new FixConvertor();
	}
	
	@Test
	public void convertFixToObjectTest() {
		String fixMessage = "8=FIX.4.435=D1=12345655=VND38=100044=20000";
		NewOrderSingle order = fixConvertor.convertFixToObject(fixMessage, NewOrderSingle.class);
		Assert.assertNotNull(order);
		Assert.assertEquals("VND", order.getSymbol());
	}
	
	@Test
	public void convertObjectToFixTest() {
		NewOrderSingle order = new NewOrderSingle();
		order.setSymbol("VND");
		order.setAccount("123456");
		order.setMsgType("D");
		order.setOrderQty(1000);
		order.setPrice(20000d);
		String fixMessExpected = "8=FIX.4.49=3835=D1=12345638=100044=2000055=VND10=100";
		String fixMessage = fixConvertor.convertObjectToFix(order);
		Assert.assertNotNull(fixMessage);
		Assert.assertEquals(fixMessExpected, fixMessage);
	}
	
	@Test
	public void convertObjectToFixSecurityStatusTest() {
		SecurityStatus securityStatus = new SecurityStatus();
		securityStatus.setSecurityTradingStatus(17);
		securityStatus.setIssueDate( new Date());
		securityStatus.setMaturityDate(new Date());
		String fixMessage = fixConvertor.convertObjectToFix(securityStatus);
		Assert.assertNotNull(fixMessage);
		System.out.println(fixMessage);
		
		/*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");		
		Date date = new Date();
		
		System.out.println(simpleDateFormat.format(date));*/
	}
}
