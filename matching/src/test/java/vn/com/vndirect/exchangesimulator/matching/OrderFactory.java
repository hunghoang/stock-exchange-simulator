package vn.com.vndirect.exchangesimulator.matching;

import java.util.UUID;

import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;

public class OrderFactory {
	
	public static NewOrderSingle createOrder(String account, char side, char ordType, String symbol, int quantity, int price) {
		NewOrderSingle order = new NewOrderSingle();
		order.setSide(side);
		order.setOrdType(ordType);
		order.setAccount(account);
		order.setSymbol(symbol);
		order.setOrderQty(quantity);
		order.setPrice((double)price);
		order.setSenderCompID(UUID.randomUUID().toString());
		order.setMsgSeqNum(1);
		return order;
	}
	
	
	public static NewOrderSingle createLOOrder(String account, char side, String symbol, int quantity, int price){
		return createOrder(account, side, '2', symbol, quantity, price);
	}
	
	public static NewOrderSingle createMTLOrder(String account, char side, String symbol, int quantity){
		return createOrder(account, side, 'T', symbol, quantity, 0);
	}

	public static NewOrderSingle createMOKOrder(String account, char side, String symbol, int quantity){
		return createOrder(account, side, 'K', symbol, quantity, 0);
	}
	
	public static NewOrderSingle createMAKOrder(String account, char side, String symbol, int quantity){
		return createOrder(account, side, 'A', symbol, quantity, 0);
	}
	
	public static NewOrderSingle createATCOrder(String account, char side, String symbol, int quantity){
		return createOrder(account, side, '5', symbol, quantity, 0);
	}

	public static NewOrderSingle createLOBuy(String symbol, int quantity, int price) {
		return OrderFactory.createLOOrder(String.valueOf(Math.random()), NewOrderSingle.BUY,
				symbol, quantity, price);
	}
	
	public static NewOrderSingle createMTLBuy(String symbol, int quantity) {
		return OrderFactory.createMTLOrder(String.valueOf(Math.random()), NewOrderSingle.BUY, symbol, quantity);
	}
	
	public static NewOrderSingle createMOKSell(String symbol, int quantity) {
		return OrderFactory.createMOKOrder(String.valueOf(Math.random()), NewOrderSingle.SELL, symbol, quantity);
	}
	
	public static NewOrderSingle createMOKBuy(String symbol, int quantity) {
		return OrderFactory.createMOKOrder(String.valueOf(Math.random()), NewOrderSingle.BUY, symbol, quantity);
	}
	
	public static NewOrderSingle createMAKSell(String symbol, int quantity) {
		return OrderFactory.createMAKOrder(String.valueOf(Math.random()), NewOrderSingle.SELL, symbol, quantity);
	}

	public static NewOrderSingle createMAKBuy(String symbol, int quantity) {
		return OrderFactory.createMAKOrder(String.valueOf(Math.random()), NewOrderSingle.BUY, symbol, quantity);
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
