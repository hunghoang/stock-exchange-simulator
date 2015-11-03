package vn.com.vndirect.exchangesimulator.fixconvertor;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.CrossOrderCancelRequest;
import vn.com.vndirect.exchangesimulator.model.GroupSide;
import vn.com.vndirect.exchangesimulator.model.NewOrderCross;
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
	}
	
	@Test
	public void testConvertCrossOrderOneFirmFromFixMessageShouldReturnCrossOrderObject() throws NoSuchFieldException, SecurityException {
		String crossOrderMessage = "8=FIX.4.49=26435=s49=021.01GW56=HNX34=452=20151013-16:42:53.212369=1138549=1550=063=D548=01001552=254=211=01001453=1448=0211=021C01938538=50001581=254=111=01001453=1448=0211=021C00005738=50001581=260=20151013-16:42:53.21255=VND44=1110010=087";
		Object crossOrder = fixConvertor.convertFixToObject(crossOrderMessage);
		Assert.assertTrue(NewOrderCross.class.isInstance(crossOrder));
		NewOrderCross newOrderCross = (NewOrderCross) crossOrder; 
		Assert.assertEquals(2, newOrderCross.getGroupSides().size());
		GroupSide group1 = newOrderCross.getGroupSides().get(0);
		Assert.assertEquals("2", group1.getSide() + "");
		Assert.assertEquals("01001", group1.getClOrdID());
		Assert.assertEquals(new Integer(1), group1.getNoPartyIDs());
		Assert.assertEquals("021C019385", group1.getAccount());
		Assert.assertEquals("021", group1.getPartyID());
		Assert.assertEquals(new Integer(50001), group1.getOrderQty());
		Assert.assertEquals("2", group1.getAccountType());
		GroupSide group2 = newOrderCross.getGroupSides().get(1);
		Assert.assertEquals("1", group2.getSide() + "");
		Assert.assertEquals("01001", group2.getClOrdID());
		Assert.assertEquals(new Integer(1), group2.getNoPartyIDs());
		Assert.assertEquals("021C000057", group2.getAccount());
		Assert.assertEquals("021", group2.getPartyID());
		Assert.assertEquals(new Integer(50001), group2.getOrderQty());
		Assert.assertEquals("2", group2.getAccountType());
	}

	@Test
	public void testConvertCrossOrderTwoFirmFromFixMessageShouldReturnCrossOrderObject() throws NoSuchFieldException, SecurityException {
		String crossOrderMessage = "8=FIX.4.49=26435=s49=021.01GW56=HNX34=452=20151013-16:42:53.212369=1138549=1550=063=D548=01001552=254=211=01001453=1448=0211=021C01938538=50001581=254=111=01001453=1448=0211=38=50001581=260=20151013-16:42:53.21255=VND44=1110010=087";
		Object crossOrder = fixConvertor.convertFixToObject(crossOrderMessage);
		Assert.assertTrue(NewOrderCross.class.isInstance(crossOrder));
		NewOrderCross newOrderCross = (NewOrderCross) crossOrder; 
		Assert.assertEquals(2, newOrderCross.getGroupSides().size());
		GroupSide group1 = newOrderCross.getGroupSides().get(0);
		Assert.assertEquals("2", group1.getSide() + "");
		Assert.assertEquals("01001", group1.getClOrdID());
		Assert.assertEquals(new Integer(1), group1.getNoPartyIDs());
		Assert.assertEquals("021C019385", group1.getAccount());
		Assert.assertEquals("021", group1.getPartyID());
		Assert.assertEquals(new Integer(50001), group1.getOrderQty());
		Assert.assertEquals("2", group1.getAccountType());
		GroupSide group2 = newOrderCross.getGroupSides().get(1);
		Assert.assertEquals("1", group2.getSide() + "");
		Assert.assertEquals("01001", group2.getClOrdID());
		Assert.assertEquals(new Integer(1), group2.getNoPartyIDs());
		Assert.assertEquals("", group2.getAccount());
		Assert.assertEquals("021", group2.getPartyID());
		Assert.assertEquals(new Integer(50001), group2.getOrderQty());
		Assert.assertEquals("2", group2.getAccountType());
		Assert.assertEquals("VND", newOrderCross.getSymbol());
	}
	
	@Test
	public void convertCrossOrderToFixTest() {
		NewOrderCross cross = new NewOrderCross();
		GroupSide group = new GroupSide();
		group.setAccount("acc1");
		group.setClOrdID("0001");
		group.setOrderQty(2000);
		group.setSide('2');
		group.setNoPartyIDs(1);
		group.setPartyID("021C");
		GroupSide group2 = new GroupSide();
		group2.setAccount("");
		group2.setClOrdID("0002");
		group2.setOrderQty(2000);
		group2.setSide('2');
		group2.setNoPartyIDs(1);
		group2.setPartyID("022C");
		cross.addGroup(group);
		cross.addGroup(group2);
		cross.setPrice(50000);
		cross.setSymbol("VND");
		cross.setCrossID("crossID");
		String message = fixConvertor.convertObjectToFix(cross);
		System.out.println(message);
		Assert.assertTrue(message.contains("55=VND"));
		Assert.assertTrue(message.contains("1=acc1"));
		Assert.assertTrue(message.contains("1="));
		Assert.assertTrue(message.contains("11=0001"));
		Assert.assertTrue(message.contains("11=0002"));
		Assert.assertTrue(message.contains("44=5000"));
		Assert.assertTrue(message.contains("38=2000"));
	}
	
	@Test
	public void testConvertOrderCrossCancelRequestFromMessage() {
		String msg = "8=FIX.4.49=14835=u49=0211GW56=HNX34=552=20151024-15:05:44.434369=375548=ACB000001010551=ACB000001010549=155=ACB60=20151024-15:05:44.43410=224";
		Object object = fixConvertor.convertFixToObject(msg);
		Assert.assertTrue(CrossOrderCancelRequest.class.isInstance(object));
	}
}
