package vn.com.vndirect.exchangesimulator.matching;

import java.util.ArrayList;
import java.util.List;

import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCSessionMatcher {

	private ATCBestMatches bestMatches;
	private ATCVolAccumulator volCummulator;
	private ExpireReporter expireReporter;
	private int lastMatchPrice;
	private List<ExecutionReport> matchedResults;
	private List<ExecutionReport> expiredResults;
	private OrderList allOrders;
	private PriceRange range;
	private String symbol;

	public ATCSessionMatcher(PriceRange range, String symbol) {
		this.range = range;
		this.symbol = symbol;
		this.bestMatches = new ATCBestMatches(range, symbol);
		this.volCummulator = new ATCVolAccumulator(range);
		this.expireReporter = new ExpireReporter();
		allOrders = new OrderList();
	}

	public void push(NewOrderSingle order) {
		this.allOrders.add(order);
		if (order.getOrdType() == '5') {
			volCummulator.addATC(order.getSide(), order.getOrderQty());
		}
		if (order.getOrdType() == '2') {
			volCummulator.add(order.getSide(), (int) Math.round(order.getPrice()), order.getOrderQty());
		}
		bestMatches.push(order);
	}

	public void processATC() {
		matchedResults = new ArrayList<ExecutionReport>();
		int bestPrice = volCummulator.getBestPrice();
		OrderList bestBuy;
		OrderList bestSell;
		ATCReport rpt = new ATCReport(bestPrice);
		if (bestPrice == -1) {
			bestPrice = lastMatchPrice;
			bestBuy = bestMatches.getATCOrder(NewOrderSingle.BUY);
			bestSell = bestMatches.getATCOrder(NewOrderSingle.SELL);
		} else {
			bestBuy = bestMatches.getBestOrders(NewOrderSingle.BUY, bestPrice);
			bestSell = bestMatches.getBestOrders(NewOrderSingle.SELL, bestPrice);
		}
		OrderMatcher m = new OrderMatcher(rpt);
		matchedResults.addAll(m.match(bestBuy, bestSell));
		allOrders.clearEmptyOrder();
		expiredResults = expireReporter.generateReport(allOrders);

	}

	public List<ExecutionReport> getExpiredResult() {
		return expiredResults;
	}

	public List<ExecutionReport> getMatchedResult() {
		return matchedResults;
	}

	public void clear() {
		this.bestMatches = new ATCBestMatches(range, symbol);
		this.volCummulator = new ATCVolAccumulator(range);
		this.expireReporter = new ExpireReporter();
		allOrders = new OrderList();
	}

	public void setLastMatchPrice(int price) {
		this.lastMatchPrice = price;
	}

	public int getLastMatchPrice() {
		return lastMatchPrice;
	}
}
