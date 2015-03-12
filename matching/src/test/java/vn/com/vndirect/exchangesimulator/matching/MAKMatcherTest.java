package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

public class MAKMatcherTest {
	
	private ContinuousSessionMatcher sm;
	
	@Before
	public void setup() {
		sm = new ContinuousSessionMatcher("VND", new PriceRange(10000, 16300, 100), new OrderMatcher(new ContinuousReport()), new OrderPriceIndex());
	}

	@Test
	public void testGenerateReportForMAKBuyWithoutMatchingAnyLOSell() {
		NewOrderSingle MAKOrder = OrderFactory.createMAKBuy("VND", 3000);
		sm.push(MAKOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(1, reports.size());
		verifyExpiredReport(reports.get(0), 3000);
	}
	
	private void verifyExpiredReport(ExecutionReport report, double leaveQty) {
		Assert.assertEquals(OrdStatus.REJECT, report.getOrdStatus());
		Assert.assertEquals(ExecType.REJECT, report.getExecType());
		Assert.assertEquals(report.getUnderlyingLastQty(), leaveQty, 0);
	}

	@Test
	public void testGivenMAKSellWithOneLOBuyWhenQuantityIsMatched() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle makOrder = OrderFactory.createMAKSell("VND", 1000);
		sm.push(makOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(2, reports.size());
		for(ExecutionReport report : reports) {
			verifyReportFillReport(report, '2', 1000, 13000);
		}
	}
	
	@Test
	public void testGivenMAKSellWithOneLOBuyWhenQuantityOfMAKOrderIsSmaller() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle MAKOrder = OrderFactory.createMAKSell("VND", 500);
		sm.push(MAKOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(2, reports.size());
		for(ExecutionReport report : reports) {
			verifyReportFillReport(report, '2', 500, 13000);
		}
	}
	
	@Test
	public void testGivenMAKSellWithOneLOBuyWhenQuantityOfMAKOrderIsBigger() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle MAKOrder = OrderFactory.createMAKSell("VND", 1500);
		sm.push(MAKOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(3, reports.size());
		verifyReportFillReport(reports.get(0), '2', 1000, 13000);
		verifyReportFillReport(reports.get(1), '2', 1000, 13000);
		verifyExpiredReport(reports.get(2), 500);
	}
	
	
	@Test
	public void testGivenMAKSellWith2LOBuyWhenQuantityIsMatched() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle order2 = OrderFactory.createLOBuy("VND", 2000, 13500);
		sm.push(order2);
		NewOrderSingle makOrder = OrderFactory.createMAKSell("VND", 3000);
		sm.push(makOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(4, reports.size());
		verifyReportFillReport(reports.get(0), '2', 2000, 13500);
		verifyReportFillReport(reports.get(1), '2', 2000, 13500);
		verifyReportFillReport(reports.get(2), '2', 1000, 13000);
		verifyReportFillReport(reports.get(3), '2', 1000, 13000);
	}
	
	
	@Test
	public void testGivenMAKBuyWith2LOSellWhenMAKQuantityIsBigger() {
		NewOrderSingle order = OrderFactory.createLOSell("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle order2 = OrderFactory.createLOSell("VND", 2000, 13500);
		sm.push(order2);
		NewOrderSingle makOrder = OrderFactory.createMAKBuy("VND", 7000);
		sm.push(makOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(5, reports.size());
		verifyReportFillReport(reports.get(0), '2', 1000, 13000);
		verifyReportFillReport(reports.get(1), '2', 1000, 13000);
		verifyReportFillReport(reports.get(2), '2', 2000, 13500);
		verifyReportFillReport(reports.get(3), '2', 2000, 13500);
		verifyExpiredReport(reports.get(4), 4000);
	}

	
	private void verifyReportFillReport(ExecutionReport report, char status, int quantity, double price) {
		Assert.assertEquals(status, report.getOrdStatus());
		Assert.assertEquals(quantity, report.getOrderQty());
		Assert.assertEquals(quantity, report.getLastQty());
		Assert.assertEquals(price, report.getPrice(), 0);
		Assert.assertEquals(price, report.getLastPx(), 0);
	}

	private void verifyReportLOReport(ExecutionReport report, char status, int quantity, double price) {
		Assert.assertEquals(status, report.getOrdStatus());
		Assert.assertEquals(quantity, report.getOrderQty());
		Assert.assertEquals(price, report.getPrice(), 0);
	}
}
