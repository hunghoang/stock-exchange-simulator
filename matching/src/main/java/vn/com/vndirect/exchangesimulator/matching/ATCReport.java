package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCReport extends ReportGenerator {
	
	private int price;

	public ATCReport(int price){
		this.price = price;
	}

	@Override
	List<ExecutionReport> createReport(NewOrderSingle currOrder,
			NewOrderSingle nextOrder) {
		ExecutionReport rp1 = genReport(currOrder);
		ExecutionReport rp2 = genReport(nextOrder);
		int minQty = Math.min(currOrder.getOrderQty(),
				nextOrder.getOrderQty());
		rp1.setOrderQty(minQty);
		rp2.setOrderQty(minQty);
		
		char status1 = currOrder.getOrderQty() == minQty ? '2' : '1';
		char status2 = nextOrder.getOrderQty() == minQty ? '2' : '1';
		rp1.setOrdStatus(status1);
		rp2.setOrdStatus(status2);
		rp1.setPrice(price);
		rp2.setPrice(price);
		return genList(rp1, rp2);
	}

}
