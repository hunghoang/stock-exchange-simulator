package vn.com.vndirect.exchangesimulator.matching;

import java.util.Arrays;
import java.util.List;

import vn.com.vndirect.exchangesimulator.matching.index.OrderPriceIndex;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;
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
		return hasNoOrderSellMatchOrderBuy(order) || hasNoOrderBuyForMatchOrderSell(order);		
	}


	private boolean hasNoOrderBuyForMatchOrderSell(NewOrderSingle order) {
		return order.getSide() == NewOrderSingle.SELL && orderPriceIndex.getBestBuyIndex(orders) == -1;
	}


	private boolean hasNoOrderSellMatchOrderBuy(NewOrderSingle order) {
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
		ExecutionReport report = PendingNewReportGenerator.report(order);
		report.setOrdStatus(OrdStatus.REJECT);
		report.setExecType(ExecType.REJECT);
		report.setUnderlyingLastQty(order.getOrderQty());
		order.setOrderQty(0);
		return Arrays.asList(report);
	}

	@Override
	protected void processOrderAfterMatching(NewOrderSingle order, List<ExecutionReport> reports) {
		if (order.getOrderQty() > 0) {
			order.setOrdType('2');
			double matchingPrice = reports.get(reports.size() - 1).getPrice();
			if (order.getSide() == NewOrderSingle.BUY) {
				if (matchingPrice < priceRange.getCeil()) {
					order.setPrice(matchingPrice + priceRange.getPriceStep());
				} else {
					order.setPrice(priceRange.getCeil());
				}
			} else {
				if (matchingPrice > priceRange.getFloor()) {
					order.setPrice(matchingPrice - priceRange.getPriceStep());
				} else {
					order.setPrice(priceRange.getFloor());
				}
			}
			ExecutionReport report = PendingNewReportGenerator.report(order);
			report.setOrdStatus('M');
			reports.add(report);
	
			int index = priceRange.priceToIndex(order.getPrice());
			orders[index].add(order);
		}
		
	}




}
