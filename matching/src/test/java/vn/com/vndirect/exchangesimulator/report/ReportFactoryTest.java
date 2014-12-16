package vn.com.vndirect.exchangesimulator.report;
import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ReportFactoryTest {

	private NewOrderSingle sampleOrder(){
		NewOrderSingle order = new NewOrderSingle();
		order.setSymbol("VND");
		order.setOrderId("123");
		order.setSide('1');
		order.setClOrdID("124");
		return order;
	}
	
	@Test
	public void matchNewSingleOrder(){
		ExecutionReport report = ReportFactory.genMatchedReport(sampleOrder(), "37", 12.0, 1000);
		Assert.assertEquals("VND", report.getSymbol());
		Assert.assertEquals(12.0, report.getLastPx(), 0.001);
		Assert.assertEquals("124", report.getClOrdID());
		Assert.assertEquals("123", report.getOrigClOrdID());
		Assert.assertEquals("37", report.getOrderID());
		Assert.assertEquals("HNX", report.getSenderCompID());
		Assert.assertEquals(1000, report.getLastQty());
		Assert.assertEquals('3', report.getExecType());
		Assert.assertEquals('2', report.getOrdStatus());
	}

}
