package vn.com.vndirect.exchangesimulator.matching;

import java.util.UUID;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class OrderFactory {
	public static NewOrderSingle createLOOrder(String account, char side, String symbol, int quantity, int price){
		NewOrderSingle order = new NewOrderSingle();
		order.setSide(side);
		order.setOrdType('2');
		order.setAccount(account);
		order.setSymbol(symbol);
		order.setOrderQty(quantity);
		order.setPrice((double)price);
		order.setSenderCompID(UUID.randomUUID().toString());
		return order;
	}
	
	public static NewOrderSingle createMTLOrder(String account, char side, String symbol, int quantity){
		NewOrderSingle order = new NewOrderSingle();
		order.setSide(side);
		order.setOrdType('T');
		order.setAccount(account);
		order.setSymbol(symbol);
		order.setOrderQty(quantity);
		order.setSenderCompID(UUID.randomUUID().toString());
		return order;
	}
	
	public static NewOrderSingle createATCOrder(String account, char side, String symbol, int quantity){
		NewOrderSingle order = new NewOrderSingle();
		order.setSide(side);
		order.setOrdType('5');
		order.setAccount(account);
		order.setSymbol(symbol);
		order.setOrderQty(quantity);
		order.setPrice(0);
		return order;
	}

	public static NewOrderSingle createLOBuy(String symbol, int quantity, int price) {
		return OrderFactory.createLOOrder(String.valueOf(Math.random()), NewOrderSingle.BUY,
				symbol, quantity, price);
	}
	
	public static NewOrderSingle createMTLBuy(String symbol, int quantity) {
		return OrderFactory.createMTLOrder(String.valueOf(Math.random()), NewOrderSingle.BUY, symbol, quantity);
	}
	
	public static NewOrderSingle createMTLSell(String symbol, int quantity) {
		return OrderFactory.createMTLOrder(String.valueOf(Math.random()), NewOrderSingle.SELL, symbol, quantity);
	}
	
	public static NewOrderSingle createATCBuy(String symbol, int quantity) {
		return OrderFactory.createATCOrder(String.valueOf(Math.random()), NewOrderSingle.BUY,	symbol, quantity);
	}

	public static NewOrderSingle createLOBuy(int quantity, int price) {
		return createLOBuy("VND", quantity, price);
	}

	public static NewOrderSingle createATCSell(String symbol, int quantity) {
		return OrderFactory.createATCOrder(String.valueOf(Math.random()), NewOrderSingle.SELL,	symbol, quantity);
	}
	
	public static NewOrderSingle createLOSell(String symbol, int quantity, int price) {
		return OrderFactory.createLOOrder(String.valueOf(Math.random()), NewOrderSingle.SELL,
				symbol, quantity, price);
	}	
	public static NewOrderSingle createLOSell(int quantity, int price) {
		return OrderFactory.createLOSell("VND", quantity, price);
	}	
}
