package vn.com.vndirect.exchangesimulator;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.builder.FixMessageBuilder;


public class FixMessageBuilderTest {

	private FixMessageBuilder tcpReceiver;;
	
	@Before
	public void setUp() {
		tcpReceiver = new FixMessageBuilder();
	}
	@Test
	public void testParseMessage() {
		List<String> messages = tcpReceiver.build("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138".getBytes());
		String expected = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138";
		assertEquals(expected, messages.get(0));
	}
	
	@Test	public void testReadByteStream(){
		byte[] bytes = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138".getBytes();
		List<String> messages = tcpReceiver.build(bytes);
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
	}
	
	@Test
	public void testReadByteStreamWithMoreThanOneMessage(){
		byte[] bytes = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=83".getBytes();
		List<String> messages = tcpReceiver.build(bytes);
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
	}
	
	@Test
	public void testReadByteStreamWithTwoMessage(){
		byte[] bytes = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138".getBytes();
		List<String> messages = tcpReceiver.build(bytes);
		assertEquals(2, messages.size());
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(1));
	}
	
	@Test
	public void testReadByteStreamWithTwoMessageAndAHalf(){
		byte[] bytes = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01G".getBytes();
		List<String> messages = tcpReceiver.build(bytes);
		assertEquals(2, messages.size());
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(1));
	}
	
	@Test
	public void testReadByteStream_HalfAndAHalf(){
		byte[] bytes = "8=FIX.4.49=".getBytes();
		List<String> messages = tcpReceiver.build(bytes);
		assertTrue(messages.size() == 0);
		bytes = "7935=g49=021.01GW56=HNX34=452=xx:yy:zz369=043=N339=1335=0263=010=206".getBytes();
		messages = tcpReceiver.build(bytes);
		assertEquals(1, messages.size());
		assertEquals("8=FIX.4.49=7935=g49=021.01GW56=HNX34=452=xx:yy:zz369=043=N339=1335=0263=010=206", messages.get(0));
		
	}
	
	@Test
	public void testReadByteStreamWith6Message(){
		byte[] bytes = "8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=1388=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138".getBytes();
		List<String> messages = tcpReceiver.build(bytes);
		assertEquals(6, messages.size());
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(1));
		
		messages = tcpReceiver.build(bytes);
		
		assertEquals(6, messages.size());
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(1));
		messages = tcpReceiver.build(bytes);
		
		assertEquals(6, messages.size());
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(0));
		assertEquals("8=FIX.4.49=8335=A49=021.01GW56=HNX34=052=xx:yy:zz369=098=0108=30553=021.01GW554=27216810=138", messages.get(1));
		
	}
}
