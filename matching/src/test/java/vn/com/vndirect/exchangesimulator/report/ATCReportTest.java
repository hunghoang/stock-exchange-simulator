package vn.com.vndirect.exchangesimulator.report;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.matching.ATCReport;
import vn.com.vndirect.exchangesimulator.matching.OrderFactory;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCReportTest {

	@Test
	public void testATCGenerateReportWithDifferentCompIdShouldReturn2Report() {
		ATCReport atcReport = new ATCReport(20000);
		NewOrderSingle buyOrder = OrderFactory.createLOBuy("VND", 100, 20);
		buyOrder.setOrderId("buyOrderId");
		NewOrderSingle sellOrder = OrderFactory.createLOSell("VND", 100, 20);
		sellOrder.setOrderId("sellOrderId");

		List<ExecutionReport> reports = atcReport.createReport(buyOrder,
				sellOrder);
		Assert.assertEquals(2, reports.size());

		for (ExecutionReport executionReport : reports) {
			Assert.assertEquals("sellOrderId", executionReport.getOrigClOrdID());
			Assert.assertEquals("buyOrderId",
					executionReport.getSecondaryClOrdID());
			Assert.assertEquals(ExecType.FILL, executionReport.getExecType());
			Assert.assertEquals(100, executionReport.getLastQty());
			Assert.assertEquals(20000, executionReport.getLastPx(), 0);
			Assert.assertEquals('2', executionReport.getOrdStatus());
			Assert.assertEquals(100, executionReport.getOrderQty());
		}
	}

	@Test
	public void testATCGenerateReportWithSameCompIdShouldReturnOneReport() {
		ATCReport atcReport = new ATCReport(20000);
		NewOrderSingle buyOrder = OrderFactory.createLOBuy("VND", 100, 20);
		buyOrder.setOrderId("buyOrderId");
		buyOrder.setSenderCompID("VNDS");
		NewOrderSingle sellOrder = OrderFactory.createLOSell("VND", 100, 20);
		sellOrder.setOrderId("sellOrderId");
		sellOrder.setSenderCompID("VNDS");
		List<ExecutionReport> reports = atcReport.createReport(buyOrder,
				sellOrder);
		Assert.assertEquals(1, reports.size());
		ExecutionReport executionReport = reports.get(0);

		Assert.assertEquals("sellOrderId", executionReport.getOrigClOrdID());
		Assert.assertEquals("buyOrderId", executionReport.getSecondaryClOrdID());
		Assert.assertEquals(ExecType.FILL, executionReport.getExecType());
		Assert.assertEquals(100, executionReport.getLastQty());
		Assert.assertEquals(20000, executionReport.getLastPx(), 0);
		Assert.assertEquals('2', executionReport.getOrdStatus());
		Assert.assertEquals(100, executionReport.getOrderQty());
	}

}
