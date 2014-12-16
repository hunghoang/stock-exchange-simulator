package vn.com.vndirect.exchangesimulator.report;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.matching.ContinuousReport;
import vn.com.vndirect.exchangesimulator.matching.OrderFactory;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ContinuousReportTest {

	@Test
	public void testGenerateExecutionReport() {
		ContinuousReport continuousReport = new ContinuousReport();
		NewOrderSingle buyOrder = OrderFactory.createLOBuy("VND", 100, 20);
		buyOrder.setOrderId("buyOrderId");
		NewOrderSingle sellOrder = OrderFactory.createLOSell("VND", 100, 20);
		sellOrder.setOrderId("sellOrderId");
		List<ExecutionReport> reports = continuousReport.createReport(buyOrder , sellOrder);
		Assert.assertEquals(2, reports.size());
		for (ExecutionReport executionReport : reports) {
			Assert.assertEquals("sellOrderId", executionReport.getOrigClOrdID());
			Assert.assertEquals("buyOrderId", executionReport.getSecondaryClOrdID());
			Assert.assertEquals('3', executionReport.getExecType());
			Assert.assertTrue(executionReport.getLastQty() > 0);
			Assert.assertEquals('2', executionReport.getOrdStatus());
			Assert.assertTrue(executionReport.getSide() == '1' || executionReport.getSide() == '2');
		}
	}
}
