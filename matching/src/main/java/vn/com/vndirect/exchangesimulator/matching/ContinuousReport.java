package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ContinuousReport extends ReportGenerator {

	@Override
	public List<ExecutionReport> createReport(NewOrderSingle currOrder, NewOrderSingle nextOrder) {
		
		ExecutionReport rp1 = genReport(currOrder);
		
		String sellId = getSellId(currOrder, nextOrder);
		String buyId = getBuyId(currOrder, nextOrder);

		rp1.setOrigClOrdID(sellId);
		rp1.setSecondaryClOrdID(buyId);
		

		int minQty = Math.min(currOrder.getOrderQty(), nextOrder.getOrderQty());
		rp1.setOrderQty(minQty);
		
		rp1.setLastQty(minQty);
		
		rp1.setOrdStatus('2');
		
		rp1.setPrice(currOrder.getPrice());
		
		rp1.setLastPx(currOrder.getPrice());
		
		if (!currOrder.getSenderCompID().equals(nextOrder.getSenderCompID())) {
			ExecutionReport rp2 = genReport(nextOrder);
			rp2.setOrigClOrdID(sellId);
			rp2.setSecondaryClOrdID(buyId);
			rp2.setOrderQty(minQty);
			rp2.setLastQty(minQty);
			rp2.setOrdStatus('2');
			rp2.setPrice(currOrder.getPrice());
			rp2.setLastPx(currOrder.getPrice());
			return genList(rp1, rp2);
		}
		return genList(rp1);
	}

	private String getBuyId(NewOrderSingle currOrder, NewOrderSingle nextOrder) {
		return currOrder.getSide() == NewOrderSingle.BUY ? currOrder.getOrderId() : nextOrder.getOrderId();
	}

	private String getSellId(NewOrderSingle currOrder, NewOrderSingle nextOrder) {
		return currOrder.getSide() == NewOrderSingle.SELL ? currOrder.getOrderId() : nextOrder.getOrderId();
	}

}
