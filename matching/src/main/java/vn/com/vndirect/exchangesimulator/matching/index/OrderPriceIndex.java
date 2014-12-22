package vn.com.vndirect.exchangesimulator.matching.index;

import vn.com.vndirect.exchangesimulator.matching.OrderList;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class OrderPriceIndex {
	public int getBestBuyIndex(OrderList[] orders) {
		for (int i = orders.length - 1; i >= 0; i--) {
			if (orders[i].size() > 0 && orders[i].get(0).getSide() == NewOrderSingle.BUY) {
				return i;
			}
		}
		return -1;
	}

	public int getBestSellIndex(OrderList[] orders) {
		for (int i = 0; i < orders.length; i++) {
			if (orders[i].size() > 0 && orders[i].get(0).getSide() == NewOrderSingle.SELL) {
				return i;
			}
		}
		return orders.length;
	}
}
