package vn.com.vndirect.exchangesimulator.matching;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class BestOrderQueue {
	
	private OrderList[] orderList;
	private PriceRange range;
	private char side;

	public BestOrderQueue(char side, PriceRange range, String symbol){
		this.side = side;
		this.range = range;
		orderList = new OrderList[range.getStepCount()];
		for(int i = 0; i < range.getStepCount(); i++){
			orderList[i] = new OrderList();
		}
	}
	
	private int getBestIndex(){
		if(side == NewOrderSingle.BUY){
			for(int i = range.getStepCount() - 1; i >= 0; i--){
				if(orderList[i].size() > 0){
					return i;
				}
			}
		}
		
		if(side == NewOrderSingle.SELL){
			for(int i =0; i < range.getStepCount(); i++){
				if(orderList[i].size() > 0){
					return i;
				}
			}
		}
		throw new RuntimeException("No best price exist");
	}
	
	public int getBestPrice(){
		return range.indexToPrice(getBestIndex());
	}
	
	public void add(NewOrderSingle order){
		orderList[range.priceToIndex(order.getPrice())].add(order);
	}
	
	public OrderList getOrderList(int price){
		return orderList[range.priceToIndex(price)];
	}
	
	public OrderList getBestOrderList(){
		OrderList result = new OrderList();
		for (OrderList orders : orderList) {
			result.addAll(orders);
		}
		return result;
	}
	
	public OrderList getBestOrderList(int price){
		int indexOfBestPrice = range.priceToIndex(price);
		OrderList result = new OrderList();
		int start = 0;
		int end = 0;
		if (side == NewOrderSingle.BUY) {
			start = indexOfBestPrice;
			end = range.getStepCount() - 1;
		}
		if (side == NewOrderSingle.SELL) {
			start = 0;
			end = indexOfBestPrice + 1;
		}
		for (int i = start; i < end; i++){
			result.addAll(orderList[i]);
		}
		
		return result;
	}
}
