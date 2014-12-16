package vn.com.vndirect.exchangesimulator.matching;

import java.util.HashMap;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class ATCBestMatches {
	private HashMap<Character, BestOrderQueue> bestOrders = new HashMap<>();
	private HashMap<Character, OrderList> ATCOrders = new HashMap<>();
	private PriceRange range;
	private String symbol;
	
	public ATCBestMatches(PriceRange range, String symbol){
		this.range = range;
		this.symbol = symbol;
		clear();
	}
	
	public void push(NewOrderSingle order) {
		if(order.getOrdType() == '5'){
			ATCOrders.get(order.getSide()).add(order);
		}
		
		if(order.getOrdType() == '2'){
			bestOrders.get(order.getSide()).add(order);
		}
		
		if(order.getOrdType() != '5' && order.getOrdType() != '2'){
			throw new RuntimeException();
		}
	}
	
	public void clear(){
		bestOrders.put(NewOrderSingle.BUY, new BestOrderQueue(NewOrderSingle.BUY, range, symbol));
		bestOrders.put(NewOrderSingle.SELL, new BestOrderQueue(NewOrderSingle.SELL, range, symbol));
		ATCOrders.put(NewOrderSingle.BUY, new OrderList());
		ATCOrders.put(NewOrderSingle.SELL, new OrderList());
	}
	
	public OrderList getBestOrders(char side){
		OrderList ret = new OrderList();
		ret.addAll(ATCOrders.get(side));
		ret.addAll(bestOrders.get(side).getBestOrderList());
		return ret;
	}
	
	public OrderList getATCOrder(char side) {
		return ATCOrders.get(side);
	}
	
	public OrderList getBestOrders(char side,int price){
		OrderList ret = new OrderList();
		ret.addAll(ATCOrders.get(side));
		ret.addAll(bestOrders.get(side).getBestOrderList(price));
		return ret;
	}

}
