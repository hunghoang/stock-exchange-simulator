package vn.com.vndirect.exchangesimulator.matching;

import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;

public class ContinuousReport extends ReportGenerator {

	@Override
	public List<ExecutionReport> createReport(NewOrderSingle currOrder, NewOrderSingle nextOrder) {
		
		ExecutionReport rp1 = genReport(currOrder);
		
		String sellId = getSellId(currOrder, nextOrder);
		String buyId = getBuyId(currOrder, nextOrder);

		rp1.setOrigClOrdID(sellId);
		rp1.setSecondaryClOrdID(buyId);
		

		double price = currOrder.getPrice();
		int minQty = Math.min(currOrder.getOrderQty(), nextOrder.getOrderQty());
		rp1.setOrderQty(minQty);
		
		rp1.setLastQty(minQty);
		
		rp1.setOrdStatus(OrdStatus.FILL);
		rp1.setPrice(price);
		
		rp1.setLastPx(price);
		
		if (!currOrder.getSenderCompID().equals(nextOrder.getSenderCompID())) {
			ExecutionReport rp2 = genReport(nextOrder);
			rp2.setOrigClOrdID(sellId);
			rp2.setSecondaryClOrdID(buyId);
			rp2.setOrderQty(minQty);
			rp2.setLastQty(minQty);
			rp2.setOrdStatus(OrdStatus.FILL);
			rp2.setPrice(price);
			rp2.setLastPx(price);
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
