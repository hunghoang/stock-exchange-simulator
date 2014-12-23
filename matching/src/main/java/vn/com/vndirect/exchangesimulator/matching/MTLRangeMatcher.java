package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.processor.PendingNewReportGenerator;

public class MTLRangeMatcher extends RangeMatcher {

	private OrderPriceIndex orderPriceIndex;
	private PriceRange priceRange;

	public MTLRangeMatcher(PriceRange priceRange, OrderList[] orders, OrderPriceIndex orderPriceIndex, OrderMatcher orderMatcher) {
		this.priceRange = priceRange;
		this.orderPriceIndex = orderPriceIndex;
		this.orders = orders;
		this.matcher = orderMatcher;
	}
	

	@Override
	protected boolean hasNoMatch(NewOrderSingle order) {
		return hasNoOrderSellForMTLBuy(order) || hasNoOrderBuyForMTLSell(order);		
	}


	private boolean hasNoOrderBuyForMTLSell(NewOrderSingle order) {
		return order.getSide() == NewOrderSingle.SELL && orderPriceIndex.getBestBuyIndex(orders) == -1;
	}


	private boolean hasNoOrderSellForMTLBuy(NewOrderSingle order) {
		return order.getSide() == NewOrderSingle.BUY && orderPriceIndex.getBestSellIndex(orders) == orders.length;
	}


	@Override
	protected int[] getMatchingRange(NewOrderSingle order) {
		int start = 0, end = -1, direction = 0;
		if (order.getSide() == NewOrderSingle.BUY) {
			start = orderPriceIndex.getBestSellIndex(orders);
			end = orders.length - 1;
			direction = 1;
		}

		if (order.getSide() == NewOrderSingle.SELL) {
			start = orderPriceIndex.getBestBuyIndex(orders);
			end = 0;
			direction = -1;
		}
		return new int[] { start, end, direction };
	}

	@Override
	protected List<ExecutionReport> processOrderIfNoMatch(NewOrderSingle order) {
		order.setOrderQty(0);
		// TODO: GENERATE CANCEL MTL REPORT
		return new ArrayList<ExecutionReport>();
	}

	@Override
	protected void processOrderAfterMatching(NewOrderSingle order, List<ExecutionReport> reports) {
		if (order.getOrderQty() > 0) {
			order.setOrdType('2');
			double matchingPrice = reports.get(0).getPrice();
			if (order.getSide() == NewOrderSingle.BUY) {
				order.setPrice(matchingPrice - priceRange.getPriceStep());
			} else {
				order.setPrice(matchingPrice + priceRange.getPriceStep());
			}
			reports.add(PendingNewReportGenerator.report(order));
		}
		
	}




}
