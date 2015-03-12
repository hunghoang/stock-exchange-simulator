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

public class MOKMatcherTest {
	
	private ContinuousSessionMatcher sm;
	
	@Before
	public void setup() {
		sm = new ContinuousSessionMatcher("VND", new PriceRange(10000, 16300, 100), new OrderMatcher(new ContinuousReport()), new OrderPriceIndex());
	}

	@Test
	public void testGenerateReportForMOKBuyWithoutMatchingAnyLOSell() {
		NewOrderSingle mokOrder = OrderFactory.createMOKBuy("VND", 3000);
		sm.push(mokOrder);
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
	public void testGivenMOKSellWithOneLOBuyWhenQuantityIsMatched() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle mokOrder = OrderFactory.createMOKSell("VND", 1000);
		sm.push(mokOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(2, reports.size());
		for(ExecutionReport report : reports) {
			verifyReportFillReport(report, '2', 1000, 13000);
		}
	}
	
	@Test
	public void testGivenMOKSellWithOneLOBuyWhenQuantityOfMOKOrderIsSmaller() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle mokOrder = OrderFactory.createMOKSell("VND", 500);
		sm.push(mokOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(2, reports.size());
		for(ExecutionReport report : reports) {
			verifyReportFillReport(report, '2', 500, 13000);
		}
	}
	
	@Test
	public void testGivenMOKSellWithOneLOBuyWhenQuantityOfMOKOrderIsBigger() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle mokOrder = OrderFactory.createMOKSell("VND", 1500);
		sm.push(mokOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(1, reports.size());
		verifyExpiredReport(reports.get(0), 1500);
	}
	
	
	@Test
	public void testGivenMOKSellWith2LOBuyWhenQuantityIsMatched() {
		NewOrderSingle order = OrderFactory.createLOBuy("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle order2 = OrderFactory.createLOBuy("VND", 2000, 13500);
		sm.push(order2);
		NewOrderSingle mokOrder = OrderFactory.createMOKSell("VND", 3000);
		sm.push(mokOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(4, reports.size());
		verifyReportFillReport(reports.get(0), '2', 2000, 13500);
		verifyReportFillReport(reports.get(1), '2', 2000, 13500);
		verifyReportFillReport(reports.get(2), '2', 1000, 13000);
		verifyReportFillReport(reports.get(3), '2', 1000, 13000);
	}
	
	
	@Test
	public void testGivenMOKBuyWith2LOSellWhenMOKQuantityIsBigger() {
		NewOrderSingle order = OrderFactory.createLOSell("VND", 1000, 13000);
		sm.push(order);
		NewOrderSingle order2 = OrderFactory.createLOSell("VND", 2000, 13500);
		sm.push(order2);
		NewOrderSingle mokOrder = OrderFactory.createMOKBuy("VND", 7000);
		sm.push(mokOrder);
		List<ExecutionReport> reports = sm.getLastMatches();
		Assert.assertEquals(1, reports.size());
		verifyExpiredReport(reports.get(0), 7000);
	}

	
	private void verifyReportFillReport(ExecutionReport report, char status, int quantity, double price) {
		Assert.assertEquals(status, report.getOrdStatus());
		Assert.assertEquals(quantity, report.getOrderQty());
		Assert.assertEquals(quantity, report.getLastQty());
		Assert.assertEquals(price, report.getPrice(), 0);
		Assert.assertEquals(price, report.getLastPx(), 0);
	}
	
}
