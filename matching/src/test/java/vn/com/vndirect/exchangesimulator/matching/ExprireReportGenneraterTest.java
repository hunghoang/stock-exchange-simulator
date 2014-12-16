package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ExprireReportGenneraterTest {
	@Test
	public void testGenenrateReport() {
		ExpireReporter reporter = new ExpireReporter();
		OrderList orders = new OrderList(); 
		NewOrderSingle order1 = OrderFactory.createLOBuy(1000, 16200); 
		NewOrderSingle order2 = OrderFactory.createLOBuy(1000, 16200);
		NewOrderSingle order3 = OrderFactory.createLOBuy(0, 16200);
		
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		List<ExecutionReport> reports = reporter.generateReport(orders);
		Assert.assertEquals(2, reports.size());
		Assert.assertEquals('C', reports.get(0).getOrdStatus());
		
	}
	
}
