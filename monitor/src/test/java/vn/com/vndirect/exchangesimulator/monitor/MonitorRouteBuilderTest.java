package vn.com.vndirect.exchangesimulator.monitor;

import org.junit.Assert;
import org.junit.Test;

public class MonitorRouteBuilderTest {
	
	@Test
	public void testReplaceString() {
		String msg = "8=FIX.4.4^A9=182^A35=8^A49=HNX^A56=021.01GW^A34=1085^A369=51^A52=20140917-02:00:28^A150=3^A39=2^A41=0311GWVND8^A526=VND0211GW8^A37=VND0211GW81^A60=20140917-02:00:28^A32=100^A31=17100^A17=VND0211GW81^A55=NGC^A54=1^A10=082^A";
		msg = msg.replaceAll("\\^A", "");
		Assert.assertFalse(msg + " contain ^A ", msg.contains("^A"));
	}
	
}
